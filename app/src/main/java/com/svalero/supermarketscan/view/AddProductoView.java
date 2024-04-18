package com.svalero.supermarketscan.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.svalero.supermarketscan.contract.AddProductoContract;
import com.svalero.supermarketscan.domain.ProductoVistaBase;
import com.svalero.supermarketscan.presenter.AddProductoPresenter;
import com.svalero.supermarketscan.util.ImageUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddProductoView extends AppCompatActivity implements AddProductoContract.View {

    // Código para solicitud de permisos de cámara
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    // Declara variables para la vista previa de la cámara, un TextView para mostrar texto detectado, y un futuro para el proveedor de la cámara.
    private PreviewView previewView;
    private TextView detectedText;
    private TextView scanText;
    private ProgressBar progressBar;
    private boolean isPaused;
    private boolean isAddButton;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    private ProductoVistaBase producto;
    private AddProductoPresenter presenter;
    private Button addButton;
    private ImageView productImage;
    private TextView etNombre;
    private TextView etDescripcion;
    private TextView etPrecioKilo;
    private TextView etPrecio;
    private CardView lyScan;
    private Intent intent;

//    private boolean modifyBike;
//    public List<Client> clients;

//    public Button getAddButton() {
//        return addButton;
//    }

//    public void setModifyBike(boolean modifyBike) {
//        this.modifyBike = modifyBike;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_producto);
        presenter = new AddProductoPresenter(this);

        lyScan = findViewById(R.id.scan_layout);
        scanText = findViewById(R.id.scan_text);
        progressBar = findViewById(R.id.progressBar);
        productImage = findViewById(R.id.product_imageView);
        etNombre = findViewById(R.id.nombre_producto);
        etPrecio = findViewById(R.id.precio_producto);
        addButton = findViewById(R.id.add_bike_button);
        etPrecioKilo = findViewById(R.id.precio_kilo_producto);
        etDescripcion = findViewById(R.id.descripcion_producto);
        producto = new ProductoVistaBase();
        isPaused = false;
        isAddButton = false;
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

    // ESCANEAR PRODUCTO
    public void scanProduct(View view) {
        isPaused = false;
        changeAddButton(false);
        lyScan.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        cleanForms();
        Toast.makeText(this, "escaneando producto", Toast.LENGTH_SHORT).show();
    }

    // BUSCAR PRODUCTO
    private void searchProduct(String codigoBarras) {
        isPaused = true;
        changeAddButton(false);
        presenter.loadProductByQuery(codigoBarras);

        scanText.setText("Buscando...");
        lyScan.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Toast.makeText(this, "buscando producto", Toast.LENGTH_SHORT).show();
    }

    // PRODUCTO ENCONTRADO
    public void findedProduct(View view) {
        progressBar.setVisibility(View.INVISIBLE);
        scanText.setText("Toca para escanear");
    }

    // GUARDAR PRODUCTO
    public void saveProducto(View view) {
        presenter.insertProduct(producto);
        Toast.makeText(this, "producto añadido", Toast.LENGTH_SHORT).show();
    }

    // LIMPÍAR FORMULARIO
    private void cleanForms() {
        productImage.setImageResource(R.drawable.calculator_fire);
        etNombre.setText("");
        etDescripcion.setText("Apunta bien!!!");
        etPrecioKilo.setText("");
        etPrecio.setText("");
    }

    // MOSTRAR PRODUCTO
    @Override
    public void showProduct(ProductoVistaBase product) {
        productImage.setImageBitmap(ImageUtils.getBitmap(producto.getImagen()));
        etNombre.setText(product.getNombre());
        etDescripcion.setText(product.getDescripcion());
        etPrecioKilo.setText(product.getPrecioPorKg() + "€/kg");
        etPrecio.setText(product.getPrecio() + "€");

        producto = product;
        producto.setId(0);
        producto.setImagen(null);
        findedProduct(null);
        changeAddButton(true);
    }

    private void changeAddButton(boolean isAddButton) {
        this.isAddButton = isAddButton;

        if (isAddButton)
            addButton.setText(R.string.add_button);
        else
            addButton.setText(R.string.return_button);
    }

    public void clickAddButton(View view) {
        if (isAddButton) {
            saveProducto(null);
        } else {
            returnView();
        }
    }

    private void returnView() {
        Intent intent = new Intent(this, CalculadoraCompraView.class);
        startActivity(intent);
    }

    @Override
    public void showMessage(int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // CÁMARA Y PERMISOS ----------------------------------------------
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
                                System.out.println("1+++ ");
                                runOnUiThread(() -> detectedText.setText(finalResultText)); // Actualiza el TextView con el texto detectado
                                System.out.println("2+++ " + finalResultText);
                                searchProduct(finalResultText);
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

    private void handlePermissionDenial() {
        // Maneja la negativa de los permisos
        Toast.makeText(this, "Debes aceptar el uso de la cámara para escanear productos!", Toast.LENGTH_SHORT).show();
    }
}