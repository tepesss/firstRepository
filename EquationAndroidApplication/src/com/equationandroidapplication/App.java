package com.equationandroidapplication;
import com.equationandroidapplication.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class App extends Activity {
    String mResult;
    float D;
    float x1 = 0;
    float x2 = 0;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app);
        //Регистрируем кнопку
        View mSolveButton = findViewById(R.id.solve);
        mSolveButton.setOnClickListener(jhdf);
    }
    OnClickListener jhdf = new OnClickListener (){
    @Override
    public void onClick(View v) {
        showResult();
    }
    };
    private void showResult() {
        //Регистрируем поля для ввода данных
        EditText mEditA = (EditText) findViewById(R.id.a);
        EditText mEditB = (EditText) findViewById(R.id.b);
        EditText mEditC = (EditText) findViewById(R.id.c);
        //Получение введенных данных
        String mA = mEditA.getText().toString();
        String mB = mEditB.getText().toString();
        String mC = mEditC.getText().toString();
        //Получение подготовленного результата расчета
        getResult(mA, mB, mC);
        //Вывод данных
        TextView mResultField = (TextView) findViewById(R.id.resultField);
        mResultField.setText(Html.fromHtml(mResult));
    }

    private void solveEquation(int a, int b, int c) {
        //Расчет дискриминанта
        D = (float) Math.pow(b, 2) - 4 * a * c;
        //Если D < 0, то расчет не требуется
        if (D >= 0) {
        x1 = (float) ((-b + Math.sqrt(D))/(2*a));
        x2 = (float) ((-b - Math.sqrt(D))/(2*a));
        }
    } 

    private void getResult(String a, String b, String c) {
        //Объявление переменных
        int mValueA; int mValueB; int mValueC;
        //Проверяем были ли введенны данные вообще
        if (a.length() == 0) {
            a = "1"; mValueA = 1;
        } else {
            mValueA = Integer.parseInt(a);
        }
        //Достаем из строки число типа int
        if (b.length() == 0) {
            b = "0"; mValueB = 0;
        } else {
            mValueB = Integer.parseInt(b);
        }
        if (c.length() == 0) {
            c = "0"; mValueC = 0;
        } else {
            mValueC = Integer.parseInt(c);
        }
        //Передаем числовые данные функции расчета уравнения
        solveEquation(mValueA, mValueB, mValueC);
        /*Собираем строку для вывода данных * Подготовка основного уравнения * a не должно равняться 0 */
        if (mValueA == 0)
            mResult = 1 + "x2";
        else
            mResult = a + "x2";
        if (mValueB < 0 )
            mResult += b + "x";
        else
            mResult += "+" + b + "x";
        if (mValueC < 0)
            mResult += c + " = 0";
        else
            mResult += "+" + c + "= 0";
        //Вывод уравнения расчета x1 и x2
        mResult += "x1,x2 = (-(" + b + ")&plusmn√" + b + "2-4×" + a + "×" + c + ")/2×" + a + " = 0";
        /*В зависимости от значения дискриминанта добавляем данные. * D < 0 - строка c сообщением * D > 0 - результаты расчета x1 и x2 */
        if (D < 0) {
            mResult += "Уравнение не имеет решения, так как дискриминант меньше 0";
        } else {
            mResult += "x1 = -(" + b + ") + √" + D + " = " + x1 + "";
            mResult += "x2 = -(" + b + ") - √" + D + " = " + x2 + "";
        }
    }
}
 