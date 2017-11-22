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

    public <T> T getParameter(String key, Object defValue, Class<T> classOfT) {
        Object result = null;
        switch (classOfT.getSimpleName()){
            case "Boolean": result = sp.getBoolean(key, (Boolean) defValue);break;
            case "Integer": result = sp.getInt(key, (Integer) defValue); break;
            case "Long": result = sp.getLong(key, (Long) defValue); break;
            case "Float": result = sp.getFloat(key, (Float) defValue); break;
            case "String": result = sp.getString(key, (String) defValue); break;
            default: result = sp.getString(key, defValue.toString()); break;
        }
        return (T)result;
    }

    public void setParameter(String key,final Object value) {
        SharedPreferences.Editor  editor = sp.edit();
        switch (value.getClass().getSimpleName()){
            case "Boolean":editor.putBoolean(key, (Boolean) value); break;
            case "Integer":editor.putInt(key, (Integer) value); break;
            case "Long":editor.putLong(key, (Long) value); break;
            case "Float":editor.putFloat(key, (Float) value); break;
            case "String":editor.putString(key, (String) value); break;
            default:editor.putString(key, value.toString());
        }
        editor.apply();
    }
}
