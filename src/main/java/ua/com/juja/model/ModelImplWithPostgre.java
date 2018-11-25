package ua.com.juja.model;

import org.postgresql.util.PSQLException;
import ua.com.juja.model.newExceptions.NullableAnswerException;
import ua.com.juja.model.parentClassesAndInterfaces.AmstractModelWorkWithPostgre;
import ua.com.juja.view.ViewImpl;
import ua.com.juja.view.ViewInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModelImplWithPostgre extends AmstractModelWorkWithPostgre {

    @Override
    public String create(String[] params, Connection connectionToDatabase) {

        StringBuilder sqlRequest = new StringBuilder("CREATE TABLE " + params[1] + " (id SERIAL, ");
        for (int i = 2; i < params.length; i++) {
            sqlRequest = sqlRequest.append(params[i]).append(" VARCHAR(255), ");
        }
        sqlRequest = sqlRequest.append("PRIMARY KEY (id))");
        try {
            requestWithoutAnswer(connectionToDatabase, sqlRequest.toString());
            return "Таблица '" + params[1] + "' успешно создана";
        } catch (PSQLException c) {
            return "Таблица с таким именем уже существует. Введите команду 'tables'" +
                    "чтобы увидеть существующие таблицы";
        } catch (SQLException a) {
            return "Неизвестная ошибка. Обратитесь с возникшей проблемой к разработчику";
        } catch (NullPointerException b) {
            return "Вы попытались создать таблицу, не подключившись к базе данных.\n" +
                    "Подключитесь к базе данных командой\n" +
                    "connect|database|username|password";
        }

    }

    @Override
    public String tables(Connection connectionToDatabase) {
        List<String> tablenames = new ArrayList<String>();
        DatabaseMetaData databaseMetaData = null;
        ResultSet resultSet = null;

        try {
            if (connectionToDatabase == null) {
                throw new NullPointerException();
            }
            databaseMetaData = connectionToDatabase.getMetaData();
            resultSet = databaseMetaData.getTables(null, null, "%",
                    new String[]{"TABLE"});
            while (resultSet.next()) {
                tablenames.add(resultSet.getString("TABLE_NAME"));
            }
            if (tablenames.size() > 0) {
                return (tablenames.toString());
            } else {
                return "На данный момент в базе данных нет ни одной таблицы";
            }
        } catch (SQLException a) {
            a.printStackTrace();
            return "Возникли проблемы в методе Tables. " +
                    "Обратитесь к разработчику. Код ошибки: " + a.getSQLState();
        } catch (NullPointerException b) {
            return "Вы попытались получить список таблиц, не подключившись к базе данных.\n" +
                    "Подключитесь к базе данных командой\n" +
                    "connect|database|username|password";
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String clear(String[] params, Connection connectionToDatabase) {
        String sqlRequest = "DELETE FROM " + params[1];
        try {
            requestWithoutAnswer(connectionToDatabase, sqlRequest);
            return "Все данные из таблицы ".concat(params[1])
                    .concat(" были удалены");
        } catch (SQLException sqlExc) {
            return "Вы пытаетесь очистить несуществующую таблицу.\n" +
                    "Вызовите команду 'tables', чтобы увидеть, какие таблицы есть в базе данных";
        } catch (NullPointerException nullPointExc) {
            return "Вы попытались очистить таблицу, не подключившись к базе данных.\n" +
                    "Подключитесь к базе данных командой\n" +
                    "'connect|database|username|password'";
        }
    }

    @Override
    public String drop(String[] params, Connection connectionToDatabase) {
        if (params.length < 2) {
            return "Недостаточно данных для запуска команды." +
                    "Укажите имя таблицы, которое собираетесь удалить";
        } else {
            String sqlRequest = "DROP TABLE ".concat(params[1]);
            try {
                requestWithoutAnswer(connectionToDatabase, sqlRequest);
                return String.format("Таблица %s успешно удалена", params[1]);
            } catch (SQLException e) {
                return "Вы попытались удалить несуществующую таблицу.\n" +
                        "Введите команду 'tables', чтобы увидеть все созданные таблицы";
            } catch (NullPointerException e) {
                return "Вы попытались удалить таблицу, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "'connect|database|username|password'";
            }
        }
    }

    @Override
    public String find(String[] params, Connection connectionToDatabase) {
        if (params.length < 2) {
            return "Недостаточно данных для запуска команды." +
                    "Укажите имя таблицы, которую собираетесь вывести на екран";
        } else {
            //формируем запрос
            StringBuilder sqlRequestForTable = new StringBuilder("SELECT * FROM ");
            sqlRequestForTable.append(params[1]);

            try {
                return requestWithAnswer(connectionToDatabase, sqlRequestForTable.toString(),
                        sqlRequestForTable.toString());
            } catch (SQLException e) {
                return "такой таблицы не существует";
            } catch (NullPointerException e1) {
                return "Вы попытались найти таблицу, не подключившись к базе данных. Сначала подключитесь";
            }
        }
    }

    @Override
    public String insert(String[] params, Connection connectionToDatabase) {
        if (params.length < 4 && params.length % 2 != 0) {
            if (params.length < 4) {
                return "Недостаточно данных для запуска команды." +
                        "Недостаточно данных для ее выполнения. Попробуйте еще раз.";
            } else {
                return "Ошибка в формате команды." +
                        "Проверьте, указали ли вы таблицу, всем ли именам колонок соответствуют значения и наоборот";
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
                return "Все данные успешно добавлены";
                //2 варика, - неправильная база, не те параметры
            } catch (NullPointerException a) {
                return "Вы попытались вставить информацию в таблицу, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "connect|database|username|password";
            } catch (SQLException b) {
                StringBuilder causeOfError = new StringBuilder("Ошибка в работе с базой данных. Причина:\n");
                if (b.getSQLState().equals("42P01")) {
                    causeOfError.append("Таблицы '").append(params[1]).append("' не сущетвует. Переформулируйте запрос");
                } else if (b.getSQLState().equals("42703")) {
                    causeOfError.append("Среди параметров, которые нужно ввести, введено несуществующее имя колонки.\n" +
                            "Переформулируйте запрос.");
                } else {
                    causeOfError.append("Непредвиденная ошибка. Код ошибки, - ").append(b.getSQLState());
                }
                return causeOfError.toString();
            }
        }
    }

    @Override
    public String update(String[] params, Connection connectionToDatabase) {
        if (params.length < 4 && params.length % 2 != 0) {
            if (params.length < 4) {
                return "Недостаточно данных для запуска команды." +
                        "Недостаточно данных для ее выполнения. Попробуйте еще раз.";
            } else {
                return "Ошибка в формате команды." +
                        "Проверьте, указали ли вы таблицу, всем ли именам колонок соответствуют значения и наоборот";
            }
        } else {
            //UPDATE table SET column1 = value1, column2 = value2 ,... WHERE condition;
            //Формирование запроса для получения данных для таблицы
            StringBuilder sqlRequestForTable = new StringBuilder("SELECT * FROM ").append(params[1]).
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
                return "Были изменены следующие строки:\n"
                        + requestWithAnswer(connectionToDatabase, sqlRequestForWork.toString(),
                        sqlRequestForTable.toString());
            } catch (NullPointerException c) {
                return "Вы попытались обновить данные в таблице, не подключившись к базе данных. Сначала подключитесь";
            } catch (SQLException d) {
                StringBuilder causeOfError = new StringBuilder("Ошибка в работе с базой данных. Причина:\n");
                if (d.getSQLState().equals("42P01")) {
                    causeOfError.append("Таблицы '").append(params[1]).append("' не сущетвует. Переформулируйте запрос");
                } else if (d.getSQLState().equals("02000")) {
                    causeOfError.append("Запрошенных данных не существует");
                } else if (d.getSQLState().equals("42703")) {
                    causeOfError.append("Среди параметров, которые нужно изменить, введено несуществующее имя колонки.\n" +
                            "Переформулируйте запрос.");
                } else {
                    causeOfError.append("Непредвиденная ошибка. Код ошибки, - ").append(d.getSQLState());
                }
                return causeOfError.toString();
            }
        }
    }

    @Override
    public String delete(String[] params, Connection connectionToDatabase) {
        if (params.length < 4 && params.length % 2 != 0) {
            if (params.length < 4) {
                return "Недостаточно данных для запуска команды." +
                        "Недостаточно данных для ее выполнения. Попробуйте еще раз.";
            } else {
                return "Ошибка в формате команды." +
                        "Проверьте, указали ли вы таблицу, всем ли именам колонок соответствуют значения и наоборот";
            }
        } else {
            //Готовим запрос для вывода таблицыю
            StringBuilder sqlReqForTable = new StringBuilder("SELECT * FROM ").append(params[1]).
                    append(" WHERE ").append(params[2]).append(" ='" + params[3] + "'");
            //Готовим запрос для удаления данных, подходящих под условия
            StringBuilder sqlForWork = new StringBuilder("DELETE FROM ".concat(params[1].concat(" WHERE ").
                    concat(params[2]).concat(" ='" + params[3] + "'")));
            try {
                return "Были удалены следующие строки:\n"
                        + requestWithAnswer(connectionToDatabase, sqlForWork.toString(),
                        sqlReqForTable.toString());
            } catch (SQLException a) {
                StringBuilder causeOfError = new StringBuilder("Ошибка в работе с базой данных. Причина:\n");
                if (a.getSQLState().equals("42P01")) {
                    causeOfError.append("Таблицы '").append(params[1]).append("' не сущетвует. Переформулируйте запрос");
                } else if (a.getSQLState().equals("02000")) {
                    causeOfError.append("Запрошенных данных не существует");
                } else if (a.getSQLState().equals("42703")) {
                    causeOfError.append("Среди параметров, которые нужно изменить, введено несуществующее имя колонки.\n" +
                            "Переформулируйте запрос.");
                } else {
                    causeOfError.append("Непредвиденная ошибка. Код ошибки, - ").append(a.getSQLState());
                }
                return causeOfError.toString();
            } catch (NullPointerException e1) {
                return "Вы попытались удалить информацию из таблицы, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "connect|database|username|password";
            }
        }
    }
}

