package student.uts.edu.au.baristabrosapp;

public class TempNotes {

    /**
     Change Toast Colours without any additional Layouts, 2018

     This is a very easy way I've found of changing the colour of the actual image background of the Toast as well as the text colour, it doesn't require any additional layouts or any XML changes:

     Toast toast = Toast.makeText(context, message, duration);
     View view = toast.getView();

     //Gets the actual oval background of the Toast then sets the colour filter
     view.getBackground().setColorFilter(YOUR_BACKGROUND_COLOUR, PorterDuff.Mode.SRC_IN);
     f
     //Gets the TextView from the Toast so it can be editted
     TextView text = view.findViewById(android.R.id.message);
     text.setTextColor(YOUR_TEXT_COLOUR);

     toast.show();

     */
}
