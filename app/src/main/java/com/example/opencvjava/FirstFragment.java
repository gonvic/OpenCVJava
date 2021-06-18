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

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class FirstFragment extends Fragment {

    // Declaration and, or initialization of variables
    View outerView;
    TextView textview_coins_number;
    TextView textview_circles_number;
    ImageView imageView;
    Bitmap originalBitmap, grayBitmap, blurBitmap, circlesBitmap, coinsBitmap;
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
        // To load the library before doing anything else
        if (OpenCVLoader.initDebug()) {
            Log.d("Check", "OpenCV configured successfully");
        } else {
            Log.d("Check", "OpenCV is not configured successfully");
        }
        // to be able to change views inside functions in this function
        // that get view as a parameter (since both functions parameter names are the same)
        outerView = view;
        // disable previous button at start
        view.findViewById(R.id.button_previous).setEnabled(false);
        // set textviews
        textview_coins_number = view.findViewById(R.id.textview_coins_number);
        textview_circles_number = view.findViewById(R.id.textview_circles_number);
        // get coins resource image as a bitmap
        originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.coins);
        // Make a mutable bitmap to copy grayscale, blur, circles, coins image
        grayBitmap =  originalBitmap.copy(originalBitmap.getConfig(), true);
        blurBitmap =  originalBitmap.copy(originalBitmap.getConfig(), true);
        circlesBitmap =  originalBitmap.copy(originalBitmap.getConfig(), true);
        coinsBitmap =  originalBitmap.copy(originalBitmap.getConfig(), true);
        // set imageView
        imageView = view.findViewById(R.id.imageview_coins);
        // function to discover the total value of coins in the bitmap/image
        findCoinsValue(originalBitmap);
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
                        imageView.setImageBitmap(grayBitmap);
                        break;
                    case 2:
                        imageView.setImageBitmap(blurBitmap);
                        break;
                    case 3:
                        imageView.setImageBitmap(circlesBitmap);
                        break;
                    case 4:
                        imageView.setImageBitmap(coinsBitmap);
                        break;
                    default:
                         imageView.setImageBitmap(originalBitmap);
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
                        imageView.setImageBitmap(grayBitmap);
                        break;
                    case 2:
                        imageView.setImageBitmap(blurBitmap);
                        break;
                    case 3:
                        imageView.setImageBitmap(circlesBitmap);
                        break;
                    case 4:
                        imageView.setImageBitmap(coinsBitmap);
                        break;
                    default:
                        imageView.setImageBitmap(originalBitmap);
                        Log.d("Check", "Default break on switch!");
                }
            }
        });
    }

    public void findCoinsValue(Bitmap bitmap) {
        // Create OpenCV coins object and copy content from bitmap
        Mat originalMat = new Mat();
        // Convert an Android Bitmap image to the OpenCV Mat.
        Utils.bitmapToMat(bitmap, originalMat);
        // GRAYSCALE
        // Create OpenCV mat object and get grayscale mat
        Mat grayMat = new Mat();
        // Convert to grayscale
        Imgproc.cvtColor(originalMat, grayMat, Imgproc.COLOR_RGB2GRAY);
        // Make a mutable bitmap to copy grayscale, blur, circles, coins image
        // grayBitmap =  originalBitmap.copy(originalBitmap.getConfig(), true);
        // Converts OpenCV Mat to Android Bitmap
        Utils.matToBitmap(grayMat, grayBitmap);
        Log.d("Check", "Done converting to gray");
        // BLUR
        // Reduce the noise to avoid false circle detection (src, dst, ksize - aperture linear size; it must be odd and greater than 1, for example: 3, 5, 7 ...)
        Mat blurMat = new Mat();
        // Smooth it, otherwise a lot of false circles may be detected
        Imgproc.medianBlur(grayMat, blurMat, 25);
        // Converts OpenCV Mat to Android Bitmap
        Utils.matToBitmap(blurMat, blurBitmap);
        Log.d("Check", "Done blurring");
        // HOUGHCIRCLES
        // Create OpenCV mat object
        Mat circlesMat = new Mat();
        // Detect circles in an image // https://stackoverflow.com/questions/54380447/error-using-houghcircles-with-3-channel-input
        Imgproc.HoughCircles(blurMat, circlesMat, Imgproc.HOUGH_GRADIENT, 1, 30, 100, 50, 100, 380); // must be single channel
        // Copy original Mat object
        Mat circlesCopyMat = originalMat; // Works with any mat, but if it's gray or blur then the Scalar color are set in tones of black gray white
        // Draw the detected circles
        for (int x = 0; x < circlesMat.cols(); x++) {
            // Get x coordinates, y coordinates and radius of the circle
            double[] circleXYRadius = circlesMat.get(0, x);
            // Create a 2D point with 2 image coordinates
            Point center = new Point(Math.round(circleXYRadius[0]), Math.round(circleXYRadius[1]));
            // Create a 4-element vector BGR color/pixel values
            Scalar color = new Scalar(0, 255, 0); // in this case it's RGB
            // Design circle center circlesCopy = circlesDetected
            Imgproc.circle(circlesCopyMat, center, 25, color, 3, 8, 0);
            // Design circle outline circlesCopy = circlesDetected
            int radius = (int) Math.round(circleXYRadius[2]);
            // Change color
            color = new Scalar(255, 0, 255);
            // Draw a circle on the image
            Imgproc.circle(circlesCopyMat, center, radius, color, 4); // 3, 8, 0
        }
        // Print the number of circles found
        int numberOfCircles = circlesMat.cols();
        textview_circles_number.setText("" + numberOfCircles);
        // Converts OpenCV Mat to Android Bitmap
        Utils.matToBitmap(circlesCopyMat, circlesBitmap);
        Log.d("Check", "Done blurring");
        // COINS VALUE
        // Array of coins value // "value": 1, "radius": 20, "ratio": 1, "count": 0
        Double[][] coinsInfo = new Double[][]{
                {1.0, 20.0, 1.0, 0.0}, // 1 CZK
                {2.0, 21.5, 1.075, 0.0}, // 2 CZK 1.075 = 21.5 / 20.0 ratio-to-smallest-coin
                {5.0, 23.0, 1.15, 0.0}, // 5 CZK
                {10.0, 24.5, 1.225, 0.0}, // 10 CZK
                {20.0, 26.0, 1.3, 0.0}, // 20 CZK
                {50.0, 27.5, 1.375, 0.0} // 50 CZK
                // the radius doesn't matter for math purposes only the ratio
        };
        // Array of coins value/name
        String[] name = new String[]{
                "1 CZK",
                "2 CZK",
                "5 CZK",
                "10 CZK",
                "20 CZK",
                "50 CZK"
        };
        // Arrays for each coin coordinates and radius
        //Double[] cordX = {0.0, 0.0, 0.0, 0.0, 0.0};//, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        //Double[] cordY = {0.0, 0.0, 0.0, 0.0, 0.0};//, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        //Double[] radius = {0.0, 0.0, 0.0, 0.0, 0.0};//, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        Double[] cordX = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        Double[] cordY = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        Double[] radius = {9999999999.0, 9999999999.0, 9999999999.0, 9999999999.0, 9999999999.0, 9999999999.0, 9999999999.0, 9999999999.0, 9999999999.0, 9999999999.0, 9999999999.0, 9999999999.0, 9999999999.0, 9999999999.0, 9999999999.0, 9999999999.0, 9999999999.0}; // this only serves to declare since the values will be replaced below
        // Get coordinates and radius of each circle/coin
        for (int x = 0; x < circlesMat.cols(); x++) {
            //if (x == 5) break; // update this if or the radius array
            // Get x coordinates, y coordinates and radius of the circle
            double[] circleXYRadius = circlesMat.get(0, x);
            cordX[x] = circleXYRadius[0];
            cordY[x] = circleXYRadius[1];
            radius[x] = circleXYRadius[2]; // int radius = (int) Math.round(circleXYRadius[2]);
            Log.d("Radius", ""+radius[x]);
        }
        // To hold the smallest radius of a circle/coin
        Double smallest = 9999999999.0;
        // Get the smallest radius of a circle/coin
        for (int i = 0; i < radius.length; i++) {
            if (radius[i] < smallest) { // should use an ArrayList or something ... HashMap
                smallest = radius[i];
            }
        }
        // Tolerance for the radius difference
        Double tolerance = 0.0375;
        // Total value of the coins
        Double total_amount = 0.0;
        // Copy circlesCopyMat Mat object
        Mat coinsCircled = circlesCopyMat;
        // Get font
        int font = Imgproc.FONT_HERSHEY_SIMPLEX;
        // To get the true radius
        Double ratioToCheck = 0.0;
        // Loop through each circle/coin to get the total value
        for (int x = 0; x < circlesMat.cols(); x++) {
            //if (x == 5) break; // update this if or the radius array
            // Get x coordinates, y coordinates and radius of the circle
            double[] circleXYRadius = circlesMat.get(0, x);
            cordX[x] = circleXYRadius[0];
            cordY[x] = circleXYRadius[1];
            // "value": 1, "radius": 20, "ratio": 1, "count": 0
            ratioToCheck = circleXYRadius[2] / smallest;
            // Loop through each coin value
            Imgproc.putText(coinsCircled, "CN: "+ x, new Point(cordX[x], cordY[x]), font, 2.0, new Scalar(0, 0, 0), 5);
            for (int i = 0; i < coinsInfo.length; i++) {
                // Coin value
                if (Math.abs((ratioToCheck) - coinsInfo[i][2]) <= tolerance) {
                    coinsInfo[i][3] += 1;
                    total_amount += coinsInfo[i][0];
                }
            }
        }
        Log.d("Total Pieces", "The total amount is " + total_amount + " CZK");
        for (int i = 0; i < coinsInfo.length; i++) {
            Double pieces = coinsInfo[i][3];
            Log.d("Pieces", "" + name[i] + " amount is " + pieces + " CZK");
        }
        // Print the total value of coins found
        textview_coins_number.setText("" + total_amount);
        // Converts OpenCV Mat to Android Bitmap
        Utils.matToBitmap(coinsCircled, coinsBitmap);
        Log.d("Check", "Done blurring");
    }
}