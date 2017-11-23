import android.content.SharedPreferences;
/**
 * Created by huangxingzhan on 2017/11/21.
 */

public class Config {
    private long id;
    private boolean sex;
    private float amount;
    private String name;
    private int age;
    SharedPreferences sp  = null;

    public Config(SharedPreferences sp) {
        this.sp = sp;
    }

    public long getId() {
        return getParameter("id", id, Long.class);
    }

    public void setId(long id) {
        setParameter("id", id);
    }

    public boolean getSex() {
        return getParameter("sex", sex, Boolean.class);
    }

    public void setSex(boolean sex) {
        setParameter("sex", sex);
    }

    public float getAmount() {
        return getParameter("amount", amount, Float.class);
    }

    public void setAmount(float amount) {
        setParameter("amount", amount);
    }

    public String getName() {
        return getParameter("name", name, String.class);
    }

    public void setName(String name) {
        setParameter("name", name);
    }

    public int getAge() {
        return getParameter("age", age, Integer.class);
    }

    public void setAge(int age) {
        setParameter("age", age);
    }

    /**
     * get parameter
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @param classOfT Value Class
     * @param <T> return type
     * @return
     */
    public <T> T getParameter(String key, Object defValue, Class<T> classOfT) {
        Object result = null;
        switch (classOfT.getSimpleName()){
            case "Boolean": result = sharedPreferences.getBoolean(key, (Boolean) defValue);break;
            case "Integer": result = sharedPreferences.getInt(key, (Integer) defValue); break;
            case "Long": result = sharedPreferences.getLong(key, (Long) defValue); break;
            case "Float": result = sharedPreferences.getFloat(key, (Float) defValue); break;
            case "String": result = sharedPreferences.getString(key, (String) defValue); break;
            default: throw new IllegalArgumentException(classOfT.getSimpleName() + " is not support");
        }
        return (T)result;
    }

    /**
     * Set a value in the preferences editor, to be written back
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     */
    public void setParameter(String key,final Object value) {
        SharedPreferences.Editor  editor = sharedPreferences.edit();
        switch (value.getClass().getSimpleName()){
            case "Boolean":editor.putBoolean(key, (Boolean) value); break;
            case "Integer":editor.putInt(key, (Integer) value); break;
            case "Long":editor.putLong(key, (Long) value); break;
            case "Float":editor.putFloat(key, (Float) value); break;
            case "String":editor.putString(key, (String) value); break;
            default: throw new IllegalArgumentException(value.getClass().getSimpleName() + " is not support");
        }
        editor.commit();
    }
}
