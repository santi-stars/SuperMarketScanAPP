package com.svalero.supermarketscan.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.svalero.supermarketscan.R;
import com.svalero.supermarketscan.domain.ProductoVistaBase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddProducto extends AppCompatActivity {

    // Código para solicitud de permisos de cámara
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    // Declara variables para la vista previa de la cámara, un TextView para mostrar texto detectado, y un futuro para el proveedor de la cámara.
    private PreviewView previewView;
    private TextView detectedText;
    private TextView reScanText;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    private boolean isPaused;
    private ProductoVistaBase producto;

    private Button addButton;
    private ImageView bikeImage;
    private EditText etBrand;
    private EditText etModel;
    private EditText etLicensePlate;
    private CardView lyScan;
    private Intent intent;
//    private AddBikePresenter presenter;

    private boolean modifyBike;
//    public List<Client> clients;

    public Button getAddButton() {
        return addButton;
    }

    public void setModifyBike(boolean modifyBike) {
        this.modifyBike = modifyBike;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_producto);

        bikeImage = findViewById(R.id.bike_imageView);
        etBrand = findViewById(R.id.brand_edittext_add_bike);
        etModel = findViewById(R.id.model_edittext_add_bike);
        etLicensePlate = findViewById(R.id.license_plate_edittext_add_bike);
        addButton = findViewById(R.id.add_bike_button);
        lyScan = findViewById(R.id.scan_layout);
        reScanText = findViewById(R.id.rescan_text);
        producto = new ProductoVistaBase();
        isPaused = false;
//        presenter = new AddBikePresenter(this);
//        bikeDTO = new BikeDTO();
//        clients = new ArrayList<>();
//
//        presenter.loadClientsSpinner(); //MVP
//        intent();

        // Llama al método para verificar y solicitar permisos de cámara
        checkCameraPermission();
        // Inicializa la vista previa de la cámara y el TextView mediante búsqueda por ID en el layout.
        previewView = findViewById(R.id.previewView);
    }


    @Override
    protected void onResume() {
        super.onResume();

        checkCameraPermission();
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            proceedWithCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            proceedWithCamera();
        } else {
            handlePermissionDenial();
        }
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void proceedWithCamera() {
        scanProduct(null);
        // Obtiene una instancia del proveedor de la cámara de forma asíncrona.
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        // Añade un listener al futuro del proveedor de la cámara para configurar la cámara una vez que esté disponible.
        cameraProviderFuture.addListener(() -> {
            try {
                // Obtiene el proveedor de la cámara y llama a startCamera con él.
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCamera(cameraProvider);
            } catch (Exception e) {
                // Maneja excepciones en caso de errores al obtener el proveedor de la cámara.
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @ExperimentalGetImage
    private void startCamera(ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();

        detectedText = findViewById(R.id.codebar_edittext);

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .build();

        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), imageProxy -> {
            InputImage image = InputImage.fromMediaImage(imageProxy.getImage(), imageProxy.getImageInfo().getRotationDegrees());
            recognizer.process(image)
                    .addOnSuccessListener(text -> {
                        String fullText = text.getText();
                        // Modifica aquí la expresión regular para buscar secuencias de 13 dígitos
                        Pattern pattern = Pattern.compile("\\b(\\d{1,2})[-\\s]?(\\d{6})[-\\s]?(\\d{6})\\b");
                        Matcher matcher = pattern.matcher(fullText);
                        StringBuilder resultText = new StringBuilder();
                        while (matcher.find()) {
                            // Concatena todas las coincidencias encontradas sin espacios ni guiones
                            String cleanLine = matcher.group(1) + matcher.group(2) + matcher.group(3); // Concatenar los grupos de números sin espacios ni guiones
                            resultText.append(cleanLine).append("\n");
                        }

                        final String finalResultText = resultText.toString().trim();

                        if (!isPaused) {
                            if (finalResultText.isEmpty()) {
                                runOnUiThread(() -> detectedText.setText(getResources().getString(R.string.scan_product)));
                            } else {
                                runOnUiThread(() -> detectedText.setText(finalResultText)); // Actualiza el TextView con el texto detectado
                                findProduct();
                            }
                        }

                        imageProxy.close();
                    })
                    .addOnFailureListener(e -> {
                        // Manejo de errores
                        imageProxy.close();
                    });
        });

        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, imageAnalysis, preview);
    }

    private void findProduct() {
        isPaused = true;
        lyScan.setVisibility(View.VISIBLE);
        // progressBar visible
        // buscar producto en la API
        // progressBar invisible
        // setText con datos del producto

        // metodo addProduct

        Toast.makeText(this, "producto encontrado", Toast.LENGTH_SHORT).show();
    }

    public void scanProduct(View view) {
        isPaused = false;
        lyScan.setVisibility(View.INVISIBLE);
        // progressBar invisible

        Toast.makeText(this, "escaneando producto", Toast.LENGTH_SHORT).show();
    }

    public void addProducto(View view) {
        Toast.makeText(this, "añadir producto", Toast.LENGTH_SHORT).show();

        // guardar producto en la base de datos local
        // on success
        // scanProduct
    }

    private void handlePermissionDenial() {
        // Maneja la negativa de los permisos
        Toast.makeText(this, "Debes aceptar el uso de la cámara para escanear productos!", Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void loadClientSpinner(List<Client> clients) {
//
//        this.clients.clear();
//        this.clients.addAll(clients);
//
//        String[] arraySpinner = new String[clients.size()];
//
//        for (int i = 0; i < clients.size(); i++) {
//            arraySpinner[i] = clients.get(i).getName() + " " + clients.get(i).getSurname();
//        }
//
//        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arraySpinner);
//        clientSpinner.setAdapter(adapterSpinner);
//
//    }

//    private void intent() {
//
//        intent = getIntent();
//        modifyBike = intent.getBooleanExtra("modify_bike", false);
//
//        if (modifyBike) {
//            bikeDTO.setId(intent.getIntExtra("id", 0));
//            bikeDTO.setClient(intent.getIntExtra("clientId", 0));
//
//            etBrand.setText(intent.getStringExtra("brand"));
//            etModel.setText(intent.getStringExtra("model"));
//            etLicensePlate.setText(intent.getStringExtra("license_plate"));
//
//            addButton.setText(R.string.modify_capital);
//        }
//    }

//    @Override
//    public void addBike(View view) {
//
//        bikeDTO.setBrand(etBrand.getText().toString().trim());
//        bikeDTO.setModel(etModel.getText().toString().trim());
//        bikeDTO.setLicensePlate(etLicensePlate.getText().toString().trim());
//        bikeDTO.setClient(clients.get(clientSpinner.getSelectedItemPosition()).getId());
//        bikeDTO.setBikeImage(null);
//
//        presenter.addOrModifyBike(bikeDTO, modifyBike);
//
//    }

    //    @Override
//    public void cleanForm() {
//
//        bikeImage.setImageResource(R.drawable.calculator_fire);
//        etBrand.setText("");
//        etModel.setText("");
//        etLicensePlate.setText("");
//
//    }
// Escribe un metodo para mostrar un toast al hacer click en add_bike_button
//    @Override
//    public void showMessage(int stringRes) {
//        Toast.makeText(this, stringRes, Toast.LENGTH_SHORT).show();
//    }
}