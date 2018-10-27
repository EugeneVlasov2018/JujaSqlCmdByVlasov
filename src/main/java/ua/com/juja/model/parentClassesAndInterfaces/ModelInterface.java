package ua.com.juja.model.parentClassesAndInterfaces;

public interface ModelInterface {
    void сonnect(String[] params);

    void tables();

    void clear(String[] params);

    void drop(String[] params);

    void create(String[] params);

    void find(String[] params);

    void insert(String[] params);

    void update(String[] params);

    void delete(String[] params);

    void help();

    void exit();

    void wrongCommand();

}
