
package fun.romancemania.ninex.json;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class CatSquareImageView extends AppCompatImageView
{

    public CatSquareImageView(Context context)
    {
        super(context);
    }

    public CatSquareImageView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
    }

    public CatSquareImageView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
    }

    protected void onMeasure(int i, int j)
    {
        super.onMeasure(i, j);
        setMeasuredDimension(getMeasuredWidth(), (int) ((int) (getMeasuredWidth())/1.1));
    }
}
