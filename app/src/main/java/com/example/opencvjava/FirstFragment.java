package com.example.opencvjava;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {

    // Declaration and, or initialization of variables
    TextView textview_coins_number;
    int menu = 0;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // to be able to change views inside functions in this function
        // that get view as a parameter (since both functions parameter names are the same)
        View outerView = view;
        // disable previous button at start
        view.findViewById(R.id.button_previous).setEnabled(false);
        // initialization / (first) assignment
        textview_coins_number = view.findViewById(R.id.textview_coins_number);
        // get coins resource image as a bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.coins);
        // set imageView
        ImageView imageView = view.findViewById(R.id.imageview_coins);

        // what happens when button_next is clicked
        view.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu++;
                // enable button because there is more functions
                if (menu > 0) outerView.findViewById(R.id.button_previous).setEnabled(true);
                // disable button because there is no more functions
                if (menu == 4) outerView.findViewById(R.id.button_next).setEnabled(false);
                Log.d("Check", "Next -> Menu: " + menu);
                switch(menu) {
                    case 1:
                        convertToGray(bitmap, imageView);
                        break;
                    case 2:
                        smoothBlur(bitmap, imageView);
                        break;
                    case 3:
                        detectCircles(bitmap, imageView);
                        break;
                    case 4:
                        getCoinsValue(bitmap, imageView);
                        break;
                    default:
                         imageView.setImageBitmap(bitmap);
                        Log.d("Check", "Default break on switch!");
                }
            }
        });

        // what happens when button_previous is clicked
        view.findViewById(R.id.button_previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu--;
                // enable button because there is more functions
                if (menu < 4) outerView.findViewById(R.id.button_next).setEnabled(true);
                // disable button because there is no more functions
                if (menu == 0) outerView.findViewById(R.id.button_previous).setEnabled(false);
                Log.d("Check", "Previous -> Menu: " + menu);
                switch(menu) {
                    case 1:
                        convertToGray(bitmap, imageView);
                        break;
                    case 2:
                        smoothBlur(bitmap, imageView);
                        break;
                    case 3:
                        detectCircles(bitmap, imageView);
                        break;
                    case 4:
                        getCoinsValue(bitmap, imageView);
                        break;
                    default:
                        imageView.setImageBitmap(bitmap);
                        Log.d("Check", "Default break on switch!");
                }
            }
        });
    }

    // TODO: Complete the functions
    public void convertToGray(Bitmap bitmap, ImageView imageView) {
        Bitmap changedBitmap = null;
        // set changed bitmap
        imageView.setImageBitmap(changedBitmap);
    }

    public void smoothBlur(Bitmap bitmap, ImageView imageView) {
        Bitmap changedBitmap = null;
        // set changed bitmap
        imageView.setImageBitmap(changedBitmap);
    }

    public void detectCircles(Bitmap bitmap, ImageView imageView) {
        Bitmap changedBitmap = null;
        // set changed bitmap
        imageView.setImageBitmap(changedBitmap);
    }

    public void getCoinsValue(Bitmap bitmap, ImageView imageView) {
        Bitmap changedBitmap = null;
        // set changed bitmap
        imageView.setImageBitmap(changedBitmap);
    }

}