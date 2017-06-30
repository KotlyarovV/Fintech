package list_worker;

/**
 * Created by vitaly on 29.06.17.
 */

public class PeopleDatas {
    private String name;
    private String phone;
    private String money;

    public PeopleDatas(String name) {
        this.name = name;
    }

    public PeopleDatas(String name, double money) {
        this(name);
        this.money = String.valueOf(money);
    }

    public PeopleDatas(String name, String money) {
        this(name, Double.parseDouble(money));
    }

    public PeopleDatas (String name, String money, String phone) {
        this(name, money);
        this.phone = phone;
    }

    public String getName() {
        return this.name;
    }

    public String getMoney() {
        return money;
    }

    public String getPhone() {
        return phone;
    }
}