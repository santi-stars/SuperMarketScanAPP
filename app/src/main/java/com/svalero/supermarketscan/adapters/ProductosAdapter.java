package com.svalero.supermarketscan.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.svalero.supermarketscan.R;
import com.svalero.supermarketscan.domain.ProductoVistaBase;
import com.svalero.supermarketscan.util.ImageUtils;

import java.util.List;

public class ProductosAdapter extends BaseAdapter {

    private Context context;
    private List<ProductoVistaBase> productoVBArrayList;
    private LayoutInflater inflater;

    public ProductosAdapter(Activity context, List<ProductoVistaBase> productoVBArrayList) {
        this.context = context;
        this.productoVBArrayList = productoVBArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductoVistaBase productoVB = (ProductoVistaBase) getItem(position);

        convertView = inflater.inflate(R.layout.producto_adapter, null);
        ImageView productoVBImage = (ImageView) convertView.findViewById(R.id.product_item_imageView);
        TextView nombreTv = convertView.findViewById(R.id.product_nombre_adapter);
        TextView descTv = convertView.findViewById(R.id.product_desc_adapter);
        TextView precioCantTv = convertView.findViewById(R.id.product_cant_adapter);
        TextView precioTotalTv = convertView.findViewById(R.id.product_precio_adapter);

        if (productoVB.getImagen() != null) {
            productoVBImage.setImageBitmap(ImageUtils.getBitmap(productoVB.getImagen()));
        } else {
            productoVBImage.setImageResource(R.drawable.calculator_fire);
        }
        nombreTv.setText(productoVB.getNombre());
        descTv.setText(productoVB.getDescripcion());

        if (productoVB.getCantidad() > 1) {
            precioCantTv.setText(productoVB.getPrecio() + " x" + productoVB.getCantidad());
        } else {
            precioCantTv.setText("x" + productoVB.getCantidad());
        }

//        String total = String.valueOf(productoVB.getPrecio() * productoVB.getCantidad());
        precioTotalTv.setText((productoVB.getPrecio() * productoVB.getCantidad()) + "€");

        return convertView;
    }

    @Override
    public int getCount() {
        return productoVBArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return productoVBArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
