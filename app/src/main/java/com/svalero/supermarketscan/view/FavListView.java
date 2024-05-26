package com.svalero.supermarketscan.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.svalero.supermarketscan.R;
import com.svalero.supermarketscan.adapters.FavListAdapter;
import com.svalero.supermarketscan.contract.FavListContract;
import com.svalero.supermarketscan.domain.FavListaTotal;
import com.svalero.supermarketscan.presenter.FavListPresenter;

import java.util.ArrayList;
import java.util.List;

public class FavListView extends AppCompatActivity implements AdapterView.OnItemClickListener, FavListContract.View {

    public List<FavListaTotal> favList;
    public FavListAdapter favListAdapter;
    private FavListPresenter presenter;
    private ListView favListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_list_view);

        favList = new ArrayList<>();
        presenter = new FavListPresenter(this);
        favListView = findViewById(R.id.fav_list_lisview);

        fullScreen();
        presenter.loadFavList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.loadFavList();
        refreshList();
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

    @Override
    public void loadFavList(List<FavListaTotal> favList) {

        registerForContextMenu(favListView);    //TODO: quitar menu contextual
        this.favList = favList;

        favListAdapter = new FavListAdapter(this, this.favList);

        favListView.setAdapter(favListAdapter);
        favListView.setOnItemClickListener(this);
    }

    @Override
    public void showMessage(int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void editFavList(View view, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String currentNameList = favList.get(position).getNombreLista();
        builder.setTitle(getString(R.string.rename_list) + currentNameList + getString(R.string.a_double_dot));

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newNameList = input.getText().toString();
                saveList(newNameList, position);
            }
        });

        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void saveList(String newNameList, int position) {

        if (newNameList.trim().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.name_list_cant_be_empty);
            builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } else {
            presenter.renameList(favList.get(position).getNombreLista(), newNameList);
            favList.get(position).setNombreLista(newNameList);

            favListAdapter.notifyDataSetChanged();
            showMessage(R.string.list_renamed);
        }
    }

    public void deleteFavList(View view, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.are_you_sure_delete_list)
                .setPositiveButton(R.string.yes_capital, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nameList = favList.get(position).getNombreLista();
                        presenter.deleteList(nameList);
                        favList.remove(position);
                        refreshList();
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

//    public void productListView(View view) {
//        Intent intent = new Intent(this, CalculadoraCompraView.class);
//        startActivity(intent);
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String nameList = favList.get(position).getNombreLista();

        Bundle datos = new Bundle();
        datos.putString(getString(R.string.namelist), nameList);

        Intent intent = new Intent(this, CalculadoraCompraView.class);
        intent.putExtras(datos);
        startActivity(intent);
    }

    public void refreshList() {
        favListAdapter.notifyDataSetChanged();
    }

    public void returnTo(View view) {
        finish();
    }
}
