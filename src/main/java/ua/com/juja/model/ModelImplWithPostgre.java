package ua.com.juja.model;

import ua.com.juja.model.newExceptions.NullableAnswerException;
import ua.com.juja.model.parentClassesAndInterfaces.AmstractModelWorkWithPostgre;
import ua.com.juja.view.ViewImpl;
import ua.com.juja.view.ViewInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class    ModelImplWithPostgre extends AmstractModelWorkWithPostgre {
    private ViewInterface view = new ViewImpl();


    public ViewInterface getView() {
        return view;
    }

    //готово
    @Override
    public void create(String[] params, Connection connectionToDatabase) {
        if(params.length<3){
            view.setMessage("Недостаточно данных для запуска команды. Попробуйте еще раз");
            view.write();
        }
        else {
            StringBuilder sqlRequest = new StringBuilder("CREATE TABLE IF NOT EXISTS " + params[1] + " (id SERIAL, ");
            for (int i = 2; i < params.length; i++) {
                sqlRequest = sqlRequest.append(params[i]).append(" VARCHAR(255), ");
            }
            sqlRequest = sqlRequest.append("PRIMARY KEY (id))");
            try {
                requestWithoutAnswer(connectionToDatabase, sqlRequest.toString());
                view.setMessage("Таблица '" + params[1] + "' успешно создана");
                view.write();
            } catch (SQLException e) {
                view.setMessage("Неизвестная ошибка. Обратитесь с возникшей проблемой к разработчику");
                view.write();
                e.printStackTrace();
            } catch (NullPointerException e) {
                view.setMessage("Вы попытались создать таблицу, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "connect|database|username|password");
                view.write();
            }
        }
    }

    //готово))
    @Override
    public void tables(Connection connectionToDatabase) {
        List<String> tablenames = new ArrayList<String>();
        DatabaseMetaData databaseMetaData = null;
        try {
            databaseMetaData = connectionToDatabase.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null, null, "%",
                    new String[]{"TABLE"});
            while (resultSet.next()) {
                tablenames.add(resultSet.getString("TABLE_NAME"));
            }
            view.setMessage(tablenames.toString());
            view.write();
            resultSet.close();
        } catch (SQLException e) {
            view.setMessage("Возникли проблемы в методе Tables. " +
                    "Поверьте правильно ли вы подключились к базе");
            view.write();
        } catch (NullPointerException e1) {
            view.setMessage("Вы попытались получить список таблиц, не подключившись к базе данных.\n" +
                    "Подключитесь к базе данных командой\n" +
                    "connect|database|username|password");
            view.write();
        } catch (IndexOutOfBoundsException e2) {
            view.setMessage("В базе данныхнет ни одной таблицы");
            view.write();
        }
    }

    //готово
    @Override
    public void clear(String[] params, Connection connectionToDatabase) {
        if(params.length<2){
            view.setMessage("Недостаточно данных для запуска команды." +
                    "Укажите имя таблицы, которое собираетесь очистить");
            view.write();
        } else {
            String sqlRequest = "DELETE FROM " + params[1];
            try {
                requestWithoutAnswer(connectionToDatabase, sqlRequest.toString());
                view.setMessage("Все данные из таблицы ".concat(params[1])
                        .concat(" были удалены"));
                view.write();
            } catch (SQLException sqlExc) {
                view.setMessage("Вы пытаетесь очистить несуществующую таблицу.\n" +
                        "Вызовите команду 'tables', чтобы увидеть, какие таблицы есть в базе данных");
                view.write();
            } catch (NullPointerException nullPointExc) {
                view.setMessage("Вы попытались очистить таблицу, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "'connect|database|username|password'");
                view.write();
            }
        }
    }

    //готово
    @Override
    public void drop(String[] params, Connection connectionToDatabase) {
        if(params.length<2){
            view.setMessage("Недостаточно данных для запуска команды." +
                    "Укажите имя таблицы, которое собираетесь удалить");
            view.write();
        }
        else {
            String sqlRequest = "DROP TABLE ".concat(params[1]);
            try {
                requestWithoutAnswer(connectionToDatabase, sqlRequest);
                view.setMessage("Таблица " + params[1] + " успешно удалена");
                view.write();
            } catch (SQLException e) {
                view.setMessage("Вы попытались удалить несуществующую таблицу.\n" +
                        "Введите команду 'tables', чтобы увидеть все созданные таблицы");
                view.write();
            } catch (NullPointerException e) {
                view.setMessage("Вы попытались удалить таблицу, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "'connect|database|username|password'");
                view.write();
            }
        }
    }

    //ГОТОВО
    @Override
    public void find(String[] params, Connection connectionToDatabase) {
        if(params.length<2){
            view.setMessage("Недостаточно данных для запуска команды." +
                    "Укажите имя таблицы, которую собираетесь вывести на екран");
            view.write();
        }
        else {
            //формируем запрос
            StringBuilder sqlRequestForTable = new StringBuilder("SELECT * FROM ");
            sqlRequestForTable.append(params[1]);

            try {
                view.setMessage(requestWithAnswer(connectionToDatabase, sqlRequestForTable.toString(),
                        sqlRequestForTable.toString()));
                view.write();
            } catch (NullableAnswerException s) {
                view.setMessage("в данной таблице отсутствует информация");
                view.write();
            } catch (SQLException e) {
                view.setMessage("такой таблицы не существует");
                view.write();
            } catch (NullPointerException e1) {
                e1.printStackTrace();
                view.setMessage("Вы попытались найти таблицу, не подключившись к базе данных. Сначала подключитесь");
                view.write();
            }
        }
    }

    //готово
    @Override
    public void insert(String[] params, Connection connectionToDatabase) {
        if (params.length < 4 && params.length%2!=0) {
            if(params.length<4){
                view.setMessage("Недостаточно данных для запуска команды." +
                        "Недостаточно данных для ее выполнения. Попробуйте еще раз.");
                view.write();
            }
            else {
                view.setMessage("Ошибка в формате команды." +
                        "Проверьте, указали ли вы таблицу, всем ли именам колонок соответствуют значения и наоборот");
                view.write();
            }
        } else {
            StringBuilder mainSqlRequest = new StringBuilder("INSERT INTO ");
            //вбиваем в запрос имя базы
            mainSqlRequest.append(params[1]).append(" (");
            List<String> columnNames = new ArrayList<String>();
            List<String> columnValues = new ArrayList<String>();
            //остальную часть запроса бросаем в 2 аррая,- для названия колонок
            //и для параметров
            for (int index = 2; index < params.length; index++) {
                if (index % 2 == 0) {
                    columnNames.add(params[index].trim());
                } else {
                    columnValues.add(params[index].trim());
                }
            }
            //добавляем к запросу имена колонок
            for (int index = 0; index < columnNames.size(); index++) {
                //ниже идет такой себе костыль. Чтобы отрефракторить, - лучше разобраться с .substring()
                if (index != columnNames.size() - 1)
                    mainSqlRequest.append(columnNames.get(index)).append(", ");
                else
                    mainSqlRequest.append(columnNames.get(index));
            }
            //добавляем к запросу имена параметров
            mainSqlRequest.append(")VALUES ('");
            for (int index = 0; index < columnValues.size(); index++) {
                //ниже идет такой себе костыль. Чтобы отрефракторить, - лучше разобраться с .substring()
                if (index != columnNames.size() - 1)
                    mainSqlRequest.append(columnValues.get(index)).append("', '");
                else
                    mainSqlRequest.append(columnValues.get(index)).append("'");
            }
            mainSqlRequest.append(")");

            try {
                requestWithoutAnswer(connectionToDatabase, mainSqlRequest.toString());
                view.setMessage("Все данные успешно добавлены");
                //выполняем запрос
                view.write();
                //2 варика, - неправильная база, не те параметры
            } catch (NullPointerException nullPoint1) {
                view.setMessage("Вы попытались вставить информацию в таблицу, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "connect|database|username|password");
                view.write();
            } catch (SQLException sqlExc) {
                view.setMessage("Проблемно выполнить запрос из за неверного формата данной команды\n" +
                        "Проверьте правильность введенных:\n" +
                        "Имени таблицы\n" +
                        "Названий колонок");
                view.write();
            }
        }
    }

    //ujnjdj
    @Override
    public void update(String[] params, Connection connectionToDatabase) {
        if (params.length < 4 && params.length % 2 != 0) {
            if (params.length < 4) {
                view.setMessage("Недостаточно данных для запуска команды." +
                        "Недостаточно данных для ее выполнения. Попробуйте еще раз.");
                view.write();
            } else {
                view.setMessage("Ошибка в формате команды." +
                        "Проверьте, указали ли вы таблицу, всем ли именам колонок соответствуют значения и наоборот");
                view.write();
            }
        } else {
            //UPDATE table SET column1 = value1, column2 = value2 ,... WHERE condition;
            //Формирование запроса для получения данных для таблицы
            StringBuilder sqlReqForTable = new StringBuilder("SELECT * FROM ").append(params[1]).
                    append(" WHERE ").append(params[2]).append(" ='" + params[3] + "'");
            StringBuilder sqlRequestForWork = new StringBuilder("UPDATE ").append(params[1]).append(" SET ");
            for (int index = 4; index < params.length; index++) {
                if (index % 2 == 0)
                    sqlRequestForWork.append(params[index] + " = '");
                else
                    sqlRequestForWork.append(params[index] + "', ");
            }
            sqlRequestForWork.setLength(sqlRequestForWork.length() - 2);
            //Формирование запроса для работы метода
            sqlRequestForWork.append(" WHERE ").append(params[2]).append(" ='" + params[3] + "'");
            try {
                view.setMessage("Были изменены следующие строки:\n"
                        + requestWithAnswer(connectionToDatabase, sqlReqForTable.toString(),
                        sqlRequestForWork.toString()));
                view.write();
            } catch (NullableAnswerException a) {
                view.setMessage("Операция невыполнима, т.к. в таблице отстутствуют рядки с такими значениями");
                view.write();
            } catch (SQLException e) {
                e.printStackTrace();
                view.setMessage("такой таблицы не существует");
                view.write();
            } catch (NullPointerException e1) {
                e1.printStackTrace();
                view.setMessage("Вы попытались обновить данные в таблице, не подключившись к базе данных. Сначала подключитесь");
                view.write();
            }
        }
    }

    //Готово вот ваще
    @Override
    public void delete(String[] params, Connection connectionToDatabase) {
        if (params.length < 4 && params.length % 2 != 0) {
            if (params.length < 4) {
                view.setMessage("Недостаточно данных для запуска команды." +
                        "Недостаточно данных для ее выполнения. Попробуйте еще раз.");
                view.write();
            } else {
                view.setMessage("Ошибка в формате команды." +
                        "Проверьте, указали ли вы таблицу, всем ли именам колонок соответствуют значения и наоборот");
                view.write();
            }
        } else {
            //Готовим запрос для вывода таблицыю
            StringBuilder sqlReqForTable = new StringBuilder("SELECT * FROM ").append(params[1]).
                    append(" WHERE ").append(params[2]).append(" ='" + params[3] + "'");
            //Готовим запрос для удаления данных, подходящих под условия
            StringBuilder sqlForWork = new StringBuilder("DELETE FROM ".concat(params[1].concat(" WHERE ").
                    concat(params[2]).concat(" ='" + params[3] + "'")));
            try {
                view.setMessage("Были удалены следующие строки:\n"
                        + requestWithAnswer(connectionToDatabase, sqlReqForTable.toString(),
                        sqlForWork.toString()));
                view.write();
            } catch (NullableAnswerException a) {
                view.setMessage("Операция невыполнима, т.к. в таблице отсутствуют рядки с таким значением");
                view.write();
            } catch (SQLException e) {
                view.setMessage("такой таблицы не существует");
                view.write();
            } catch (NullPointerException e1) {
                view.setMessage("Вы попытались удалить информацию из таблицы, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "connect|database|username|password");
                view.write();
            }
        }
    }



}

