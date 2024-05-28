package com.svalero.supermarketscan.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
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
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    private ProductoVistaBase producto;
    private AddProductoPresenter presenter;
    private Button addButton;
    private Button minusButton;
    private Button plusButton;
    private ImageView productImage;
    private TextView etNombre;
    private TextView etDescripcion;
    private TextView etPrecioKilo;
    private TextView etPrecio;
    private TextView etCantidad;
    private TextView etPrecioTotal;
    private CardView lyScan;
    private int cantidad;
    private Intent intent;
    private String nameList;
    private DecimalFormat df;
    private final String DEFAULT_STRING = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_producto);
        presenter = new AddProductoPresenter(this);
        nameList = DEFAULT_STRING;
        fullScreen();
        intent();

        lyScan = findViewById(R.id.scan_layout);
        scanText = findViewById(R.id.scan_text);
        progressBar = findViewById(R.id.progressBar);
        productImage = findViewById(R.id.product_imageView);
        etNombre = findViewById(R.id.nombre_producto);
        etPrecio = findViewById(R.id.precio_producto);
        etCantidad = findViewById(R.id.cantidad_producto);
        etPrecioTotal = findViewById(R.id.precio_total_producto);
        addButton = findViewById(R.id.add_button);
        minusButton = findViewById(R.id.add_button_minus);
        plusButton = findViewById(R.id.add_button_plus);
        etPrecioKilo = findViewById(R.id.precio_kilo_producto);
        etDescripcion = findViewById(R.id.descripcion_producto);
        producto = new ProductoVistaBase();
        df = new DecimalFormat("#.##");
        cantidad = 1;
        isPaused = false;
        // Llama al método para verificar y solicitar permisos de cámara
        checkCameraPermission();
        // Inicializa la vista previa de la cámara y el TextView mediante búsqueda por ID en el layout.
        previewView = findViewById(R.id.previewView);
//        imagenesAJson();
    }

    //TODO: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    private void imagenesAJson() {
        // Configura tu ImageView aquí
        ImageView productImageView = findViewById(R.id.product_imageView);

        productImageView.setImageResource(R.drawable.arroz); // Asegúrate de que tu imagen esté cargada
        imagenesDeRecursosDrawableAStringJson(productImageView);
        productImageView.setImageResource(R.drawable.integral); // Asegúrate de que tu imagen esté cargada
        imagenesDeRecursosDrawableAStringJson(productImageView);
        productImageView.setImageResource(R.drawable.salmon); // Asegúrate de que tu imagen esté cargada
        imagenesDeRecursosDrawableAStringJson(productImageView);
        productImageView.setImageResource(R.drawable.vino); // Asegúrate de que tu imagen esté cargada
        imagenesDeRecursosDrawableAStringJson(productImageView);
        productImageView.setImageResource(R.drawable.pechuga); // Asegúrate de que tu imagen esté cargada
        imagenesDeRecursosDrawableAStringJson(productImageView);
        productImageView.setImageResource(R.drawable.atun); // Asegúrate de que tu imagen esté cargada
        imagenesDeRecursosDrawableAStringJson(productImageView);
        productImageView.setImageResource(R.drawable.manzana); // Asegúrate de que tu imagen esté cargada
        imagenesDeRecursosDrawableAStringJson(productImageView);
        productImageView.setImageResource(R.drawable.patatas); // Asegúrate de que tu imagen esté cargada
        imagenesDeRecursosDrawableAStringJson(productImageView);
        productImageView.setImageResource(R.drawable.helado); // Asegúrate de que tu imagen esté cargada
        imagenesDeRecursosDrawableAStringJson(productImageView);
        productImageView.setImageResource(R.drawable.leche); // Asegúrate de que tu imagen esté cargada
        imagenesDeRecursosDrawableAStringJson(productImageView);
        productImageView.setImageResource(R.drawable.muslitos); // Asegúrate de que tu imagen esté cargada
        imagenesDeRecursosDrawableAStringJson(productImageView);
        productImageView.setImageResource(R.drawable.huevos); // Asegúrate de que tu imagen esté cargada
        imagenesDeRecursosDrawableAStringJson(productImageView);
        productImageView.setImageResource(R.drawable.couscous); // Asegúrate de que tu imagen esté cargada
        imagenesDeRecursosDrawableAStringJson(productImageView);
    }

    private void imagenesDeRecursosDrawableAStringJson(ImageView productImageView) {
        Bitmap bitmap = getBitmapFromImageView(productImageView);
        byte[] imageBytes = ImageUtils.bitmapToByteArray(bitmap);
        String base64String = ImageUtils.encodeImageToBase64(imageBytes);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("imagen", base64String);
            String jsonString = jsonObject.toString();
            // Mostrar el JSON en la consola
            Log.d("JSON Output", jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromImageView(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();

        if (drawable instanceof BitmapDrawable)
            return ((BitmapDrawable) drawable).getBitmap();

        return null;
    }
    //TODO: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

    @Override
    protected void onResume() {
        super.onResume();

        intent();
        checkCameraPermission();
    }

    private void fullScreen() {
        // Oculta la barra de navegación y la barra de estado con el modo inmersivo pegajoso
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void intent() {
        intent = getIntent();
        String nameListIntent;

        if (intent.getStringExtra(getString(R.string.namelist)) != null) {
            nameListIntent = intent.getStringExtra(getString(R.string.namelist));

            if (nameListIntent.trim().isEmpty())
                nameList = DEFAULT_STRING;
            else
                nameList = nameListIntent;
        }
    }

    // ESCANEAR PRODUCTO
    public void scanProduct(View view) {
        cantidad = 1;
        isPaused = false;
        changeAddButton(false);
        lyScan.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        minusButton.setVisibility(View.INVISIBLE);
        plusButton.setVisibility(View.INVISIBLE);
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
    public void findedProduct(View view, boolean isFound) {
        if (isFound) {
            lyScan.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            minusButton.setVisibility(View.VISIBLE);
            plusButton.setVisibility(View.VISIBLE);
            scanText.setText("Toca para escanear");
        } else {
            lyScan.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            scanText.setText("Toca para escanear");
            etDescripcion.setText("Producto no encontrado!");
        }
    }

    // MOSTRAR PRODUCTO
    @Override
    public void showProduct(ProductoVistaBase product) {

        if (product == null) findedProduct(null, false);

        else {
            if (product.getNombre().length() >= 15)
                etNombre.setTextSize(24);

            producto = product;

            productImage.setImageBitmap(ImageUtils.getBitmapFromBase64(producto.getImagen()));
            etNombre.setText(product.getNombre());
            etDescripcion.setText(product.getDescripcion());
            etPrecioKilo.setText(df.format(product.getPrecioPorKg()) + "€/kg");
            etPrecio.setText(df.format(product.getPrecio()) + "€");
            etCantidad.setText("x" + cantidad);
            etPrecioTotal.setText(df.format(product.getPrecio() * cantidad) + "€");

            producto.setId(0);
            producto.setCantidad(cantidad);
            producto.setNombreLista(nameList);
            changeAddButton(true);
            findedProduct(null, true);
        }
    }

    // GUARDAR PRODUCTO
    public void saveProducto(View view) {
        String message = DEFAULT_STRING;
        presenter.insertProduct(producto);

        if (nameList != null)
            if (nameList.trim().isEmpty())
                message = getString(R.string.product_added);
            else
                message = getString(R.string.product_added_to_list) + nameList;

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // LIMPÍAR FORMULARIO
    private void cleanForms() {
        productImage.setImageResource(R.drawable.calculator_fire);
        etNombre.setText("");
        etDescripcion.setText("Apunta bien!!!");
        etPrecioKilo.setText("");
        etPrecio.setText("");
        etCantidad.setText("");
        etPrecioTotal.setText("");
    }

    private void changeAddButton(boolean isAddButton) {

        if (isAddButton) {
            addButton.setAlpha(1.0f);
            addButton.setClickable(true);
            addButton.setEnabled(true);
        } else {
            addButton.setAlpha(0.6f);
            addButton.setClickable(false);
            addButton.setEnabled(false);
        }
    }

    public void clickAddButton(View view) {
        saveProducto(null);
    }

    public void clickMinusButton(View view) {
        if (cantidad > 1) {
            cantidad--;
            producto.setCantidad(cantidad);
            etCantidad.setText("x" + cantidad);
            etPrecioTotal.setText(df.format(producto.getPrecio() * cantidad) + "€");
        }
    }

    public void clickPlusButton(View view) {
        cantidad++;
        producto.setCantidad(cantidad);
        etCantidad.setText("x" + cantidad);
        etPrecioTotal.setText(df.format(producto.getPrecio() * cantidad) + "€");
    }

    @Override
    public void showMessage(int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // CÁMARA Y PERMISOS ----------------------------------------------
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        else proceedWithCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            proceedWithCamera();
        else handlePermissionDenial();
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
                            String cleanLine = matcher.group(1) + matcher.group(2) + matcher.group(3); 
                            resultText.append(cleanLine).append("\n");
                        }

                        String finalResultText = resultText.toString().trim();

                        if (!isPaused) {
                            if (finalResultText.isEmpty()) {
                                runOnUiThread(() -> detectedText.setText(getResources().getString(R.string.scan_product)));
                            } else {
                                runOnUiThread(() -> detectedText.setText(finalResultText)); // Actualiza el TextView con el texto detectado
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

    public void returnTo(View view) {
        finish();
    }
}