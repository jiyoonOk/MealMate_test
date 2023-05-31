package com.anonymous.mealmate.view.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.anonymous.mealmate.viewmodel.FoodViewModel;
import com.anonymous.mealmate.viewmodel.MealSetViewModel;

public class FoodDialogFragment extends DialogFragment {

    private MealSetViewModel mealSetViewModel;

    private FoodViewModel foodViewModel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}
