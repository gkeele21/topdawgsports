package bglib.util;

public interface BGAccessLevel {
    boolean encompasses(BGAccessLevel other);
    Object getLevel();
    BGAccessLevel[] getValues();
}
