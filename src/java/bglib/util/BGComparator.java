package bglib.util;

import java.util.Comparator;

public class BGComparator implements Comparator<BGComparable> {
    String _CompareField;

    public BGComparator(String field) {
        _CompareField = FSUtils.noNull(field).replace("_", " ");
    }

    public int compare(BGComparable first, BGComparable second) {
        String[] fields = _CompareField.split(",");
        for (String theField : fields) {
            BGComparable a, b;
            String field;
            if (theField.endsWith("desc")) {
                a=second; b=first;
                field = theField.substring(0, theField.length()-5).toLowerCase(); // chop off the trailing "desc"
            } else {
                a=first; b=second; field = theField.toLowerCase();
            }

            int diff=a.compareTo(b, field);

            if (diff != 0) {
                return diff;
            }
        }

        return 0;
    }
}
