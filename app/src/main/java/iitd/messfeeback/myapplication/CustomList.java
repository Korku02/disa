package iitd.messfeeback.myapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by korku02 on 08/01/18.
 */

public class CustomList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] web1;
    private final float[] rating;

    public boolean isRatingTouched  = false;

    public CustomList(Activity context,
                      String[] web1, float[] rating) {
        super(context, R.layout.simple_list_item1, web1);
        this.context = context;
        this.web1 = web1;
        this.rating = rating;


    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder holder;
//        View rowView= inflater.inflate(R.layout.simple_list_item1, null, true);
        if (view == null) {
            view = inflater.inflate(R.layout.simple_list_item1, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
            //holder.ratingBar.getTag(position);
        }

        holder.ratingBar.setOnRatingBarChangeListener(onRatingChangedListener(holder, position));

        holder.ratingBar.setTag(position);
        holder.ratingBar.setRating(rating[position]);
        holder.txtTitle1.setText(web1[position]);

        return view;
    }

    private RatingBar.OnRatingBarChangeListener onRatingChangedListener(final ViewHolder holder, final int position) {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//                Movie item = getItem(position);
//                item.setRatingStar(v);
//                Log.i("Adapter", "star: " + v);
                rating[position] =v;
                isRatingTouched = true;


            }
        };
    }

    private static class ViewHolder {
        private RatingBar ratingBar;
        private TextView txtTitle1;

        public ViewHolder(View view) {
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar2);
            txtTitle1 = (TextView) view.findViewById(R.id.txt1);
        }
    }


}
