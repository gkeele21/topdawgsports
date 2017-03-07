package bglib.util;

public enum FormInputType {
    TEXT("text"), LISTBOX("listbox"), RADIO_BUTTON("radio"), CHECKBOX("checkbox"), PASSWORD("password"), RADIO_GROUP("radiogroup"),
    TEXT_AREA("textarea");

    String Name;

    FormInputType(String name) { Name = name; }

    public String getName() { return Name; }

    public static FormInputType valueByName(String name) {
        for (FormInputType lt : FormInputType.values()) {
            if (lt.getName().equals(name)) {
                return lt;
            }
        }

        return null;
    }

}
