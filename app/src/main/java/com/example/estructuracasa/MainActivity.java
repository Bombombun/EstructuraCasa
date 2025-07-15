package com.example.estructuracasa;

import android.os.Bundle;
import android.util.Log; // Para logs de error
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"; // Tag para logs

    EditText etLargo, etAncho, etHabitaciones, etPisos;
    Button btnCalcular;
    TextView tvResultado;
    FrameLayout contenedorPlano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLargo = findViewById(R.id.etLargo);
        etAncho = findViewById(R.id.etAncho);
        etHabitaciones = findViewById(R.id.etHabitaciones);
        etPisos = findViewById(R.id.etPisos);
        btnCalcular = findViewById(R.id.btnCalcular);
        tvResultado = findViewById(R.id.tvResultado);
        contenedorPlano = findViewById(R.id.contenedorPlano);

        // Uso de Lambda para el OnClickListener (requiere Java 8+)
        btnCalcular.setOnClickListener(view -> calcularEstructura());
    }

    private void calcularEstructura() {
        String largoStr = etLargo.getText().toString().trim();
        String anchoStr = etAncho.getText().toString().trim();
        String habitacionesStr = etHabitaciones.getText().toString().trim();
        String pisosStr = etPisos.getText().toString().trim();

        if (largoStr.isEmpty() || anchoStr.isEmpty() || habitacionesStr.isEmpty() || pisosStr.isEmpty()) {
            tvResultado.setText(getString(R.string.error_campos_incompletos));
            contenedorPlano.removeAllViews(); // Limpiar plano si hay error
            return;
        }

        try {
            double largo = Double.parseDouble(largoStr);
            double ancho = Double.parseDouble(anchoStr);
            int habitaciones = Integer.parseInt(habitacionesStr); // Se lee, pero no se usa en este cálculo. Podría usarse en PlanoEstructuralView.
            int pisos = Integer.parseInt(pisosStr);

            if (largo <= 0 || ancho <= 0 || habitaciones <= 0 || pisos <= 0) {
                tvResultado.setText(getString(R.string.error_valores_positivos));
                contenedorPlano.removeAllViews(); // Limpiar plano si hay error
                return;
            }

            // Lógica de cálculo (simplificada)
            int columnasFila = (int) (largo / 4) + 1; // Asumiendo columnas cada 4 metros + 1 en los extremos
            int columnasColumna = (int) (ancho / 4) + 1; // Asumiendo columnas cada 4 metros + 1 en los extremos
            int totalColumnas = columnasFila * columnasColumna;

            int vigasHorizontales = (columnasFila - 1) * columnasColumna;
            int vigasVerticales = (columnasColumna - 1) * columnasFila;
            int totalVigas = vigasHorizontales + vigasVerticales;

            String cimentacion;
            if (pisos <= 2) {
                cimentacion = "Zapatas aisladas";
            } else if (pisos <= 4) {
                cimentacion = "Zapatas corridas o losa de cimentación";
            } else {
                cimentacion = "Losa de cimentación o pilotes (requiere estudio especializado)";
            }

            String resultado = getString(R.string.label_columnas, totalColumnas) + "\n" +
                    getString(R.string.label_vigas, totalVigas) + "\n" +
                    getString(R.string.label_cimentacion, cimentacion);

            tvResultado.setText(resultado);

            // Actualizar el plano estructural
            contenedorPlano.removeAllViews(); // Limpiar vistas anteriores
            PlanoEstructuralView plano = new PlanoEstructuralView(this, columnasFila, columnasColumna);
            contenedorPlano.addView(plano);

        } catch (NumberFormatException e) {
            tvResultado.setText(getString(R.string.error_numeros_invalidos));
            contenedorPlano.removeAllViews(); // Limpiar plano si hay error
            Log.e(TAG, "Error de formato numérico: ", e);
        } catch (Exception e) {
            tvResultado.setText(getString(R.string.error_inesperado));
            contenedorPlano.removeAllViews(); // Limpiar plano si hay error
            Log.e(TAG, "Error inesperado en calcularEstructura: ", e); // Loguear la excepción completa
        }
    }
}
