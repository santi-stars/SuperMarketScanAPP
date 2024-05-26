package com.svalero.supermarketscan.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import com.svalero.supermarketscan.R;
import com.svalero.supermarketscan.adapters.ProductosAdapter;
import com.svalero.supermarketscan.contract.CalculadoraCompraContract;
import com.svalero.supermarketscan.domain.ProductoVistaBase;
import com.svalero.supermarketscan.presenter.CalculadoraCompraPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CalculadoraCompraView extends AppCompatActivity implements CalculadoraCompraContract.View,
        AdapterView.OnItemClickListener {

    private Intent intent;
    private String orderBy;
    private String nameList;
    private final String DEFAULT_STRING = "";
    private CalculadoraCompraPresenter presenter;
    public ProductosAdapter productosArrayAdapter;
    public List<ProductoVistaBase> productosVistaBase;

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora_compra);

        productosVistaBase = new ArrayList<>();
        presenter = new CalculadoraCompraPresenter(this);
        productosArrayAdapter = new ProductosAdapter(this, productosVistaBase);

        nameList = DEFAULT_STRING;
        intent();

        orderBy = DEFAULT_STRING;
        findProductsBy(DEFAULT_STRING);
        fullScreen();
    }

    private void fullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        intent();
        findProductsBy(DEFAULT_STRING);
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

        getTotal();
    }

    private boolean isFavList() {
        return !nameList.trim().equals(DEFAULT_STRING);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_list_actionbar, menu);

        final MenuItem searchItem = menu.findItem(R.id.app_bar_product_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                findProductsBy(query.trim());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                findProductsBy(newText.trim());
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void listProductosVB(List<ProductoVistaBase> productosVBList) {

        ListView productosListView = findViewById(R.id.product_lisview);
        registerForContextMenu(productosListView);
        this.productosVistaBase = productosVBList;

        productosArrayAdapter = new ProductosAdapter(this, this.productosVistaBase);

        productosListView.setAdapter(productosArrayAdapter);
        productosListView.setOnItemClickListener(this);

    }

    @Override
    public void showMessage(int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void refreshList() {
        productosArrayAdapter.notifyDataSetChanged();
    }

    private void findProductsBy(String query) {
        productosVistaBase.clear();

        if (query.equalsIgnoreCase(DEFAULT_STRING))
            presenter.loadAllProductosByNameList(nameList);
        else
            presenter.loadProductoByQueryAndNameList(query, nameList);

        getTotal();
        orderBy(orderBy);
    }

    private void orderBy(String orderBy) {
        this.orderBy = orderBy;

        Collections.sort(productosVistaBase, new Comparator<ProductoVistaBase>() {
            @Override
            public int compare(ProductoVistaBase o1, ProductoVistaBase o2) {
                switch (orderBy) {
                    case "asc":
                        return Double.compare(o1.getPrecio(), o2.getPrecio());
                    case "desc":
                        return Double.compare(o2.getPrecio(), o1.getPrecio());
                    case "total_asc":
                        return Double.compare(o1.getPrecio() * o1.getCantidad(), o2.getPrecio() * o2.getCantidad());
                    case "total_desc":
                        return Double.compare(o2.getPrecio() * o2.getCantidad(), o1.getPrecio() * o1.getCantidad());
                    case "nombre":
                        return o1.getNombre().compareToIgnoreCase(o2.getNombre());
                    default:
                        return String.valueOf(o1.getId()).compareTo(String.valueOf(o2.getId()));
                }
            }
        });
        refreshList();
    }

    /**
     * Opciones del menú ActionBar
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.order_by_default_item) {
            orderBy("");
        } else if (id == R.id.order_by_asc_item) {
            orderBy("asc");
        } else if (id == R.id.order_by_desc_item) {
            orderBy("desc");
        } else if (id == R.id.order_by_total_asc_item) {
            orderBy("total_asc");
        } else if (id == R.id.order_by_total_desc_item) {
            orderBy("total_desc");
        } else if (id == R.id.order_by_nombre_item) {
            orderBy("nombre");
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    /**
     * Método para cuando se crea el menu contextual, infle el menu con las opciones
     *
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.listview_menu, menu);
    }

    /**
     * Opciones del menu contextual
     *
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int id = item.getItemId();

        if (id == R.id.delete_menu) {              // Eliminar producto
            deleteProduct(info);
            return true;
        } else if (id == R.id.delete_all_menu) {              // Eliminar lista
            deleteAllProduct();
            return true;
        } else return super.onContextItemSelected(item);
    }

    private void deleteProduct(AdapterView.AdapterContextMenuInfo info) {

        ProductoVistaBase product = productosVistaBase.get(info.position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.are_you_sure_delete_product)
                .setPositiveButton(R.string.yes_capital, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.deleteProduct(product);
                        findProductsBy(DEFAULT_STRING);
                        getTotal();
                    }
                })
                .setNegativeButton(R.string.no_capital, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    private void deleteAllProduct() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.are_you_sure_delete_list)
                .setPositiveButton(R.string.yes_capital, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.deleteAllProductsByNameList(nameList);
                        findProductsBy(nameList);
                        getTotal();
                    }
                })
                .setNegativeButton(R.string.no_capital, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    private void getTotal() {
        double sum = productosVistaBase.stream()
                .mapToDouble(producto -> producto.getCantidad() * producto.getPrecio())
                .sum();
        String total = String.format(Locale.getDefault(), "%.2f", sum);
        String euroSymbol = getString(R.string.euro);
        String totalTitle = getString(R.string.total_title);
        String calculatorIcon = "🧮";
        String favIcon = "⭐";

        if (isFavList())
            Objects.requireNonNull(getSupportActionBar()).setTitle(favIcon + " " + nameList + ": " + total + euroSymbol);
        else
            Objects.requireNonNull(getSupportActionBar()).setTitle(calculatorIcon + " " + totalTitle + " " + total + euroSymbol);
    }

    public void scanProduct(View view) {
        Bundle datos = new Bundle();
        datos.putString(getString(R.string.namelist), nameList);

        Intent intent = new Intent(this, AddProductoView.class);
        intent.putExtras(datos);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    public void returnTo(View view) {
        finish();
    }

    public void saveList(View view) {
        if (productosVistaBase.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            if (isFavList())
                builder.setTitle(getString(R.string.rename_list) + nameList + getString(R.string.a_double_dot));
            else
                builder.setTitle(R.string.namelist_double_dot);

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String listName = input.getText().toString();

                    if (listName.trim().isEmpty())
                        showEmptyListNameDialog();
                    else
                        savePersonalList(listName);
                }
            });
            builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.scan_to_add_products_to_list);
            builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        }
    }

    private void showEmptyListNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.name_list_cant_be_empty);
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void savePersonalList(String listName) {
        if (productosVistaBase != null)
            for (ProductoVistaBase producto : productosVistaBase) {
                producto.setNombreLista(listName);
                presenter.updateProduct(producto);
            }

        if (isFavList())
            nameList = listName;
        else
            productosVistaBase.clear();

        getTotal();
        refreshList();
    }
}