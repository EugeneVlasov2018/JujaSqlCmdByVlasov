package ua.com.juja.model;

import org.apache.log4j.Logger;
import ua.com.juja.model.exceptions.UnknowShitException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class PostgreModel implements Model {
    private Connection connectionToDatabase;

    private static final Logger logger = Logger.getLogger(getCurrentClassName());

    @Override
    public void connect(String[] responceToDb) throws UnknowShitException {
        String url = "jdbc:postgresql://localhost:5432/" + responceToDb[1];
        String user = responceToDb[2];
        String password = responceToDb[3];
        String jdbcDriver = "org.postgresql.Driver";
        connectionToDatabase = createConnection(url, user, password, jdbcDriver);
        logger.info("Соединение с базой установлено");
    }

    private Connection createConnection(String url, String user, String password, String jdbcDriver)
            throws UnknowShitException {

        try {
            Class.forName(jdbcDriver);
            connectionToDatabase = DriverManager.getConnection(url, user, password);
            logger.debug(String.format("Получен коннекшен к базе.\n%s", connectionToDatabase.getClientInfo().toString()));
            return connectionToDatabase;
        } catch (SQLException e) {
            logger.error(String.format("Неправильный коннекшен к базе. причина:\n%s", e.getStackTrace()));
            String result = getMessage(e);
            logger.debug("сообщение о харрактере ошибки сформировано и передано в модель для дальнейшей передачи юзеру");
            throw new UnknowShitException(result);
        } catch (ClassNotFoundException a) {
            logger.error("Ошибка подключения к базе из-за отсутствующего или неправильного драйвера\n" +
                    "Проверьте наличие джарника либо депенденса в pom.xml");
            throw new UnknowShitException("Не найден драйвер подключения к базе\n" +
                    "Передайте разработчику, чтобы добавил либо джарник в либы, либо депенденс в мавен");
        }
    }

    private String getMessage(SQLException e) {
        StringBuilder result = new StringBuilder("Вы ввели: ");
        if (e.getSQLState().equalsIgnoreCase("3D000")) {
            result.append("\nНеверную ссылку на базу");
        }
        if (e.getSQLState().equalsIgnoreCase("28P01")) {
            result.append("\nНеверное имя пользователя или пароль");
        }
        result.append("\nПопробуйте снова:P");
        return result.toString();
    }

    @Override
    public void create(String[] params) throws UnknowShitException {

        String command = getSqlCommandForCreate(params);
        logger.debug(String.format("Сформирован SQL-запрос для создания таблицы в таком виде:\n%s",
                command));
        workWithDbWithoutAnswer(command);
        logger.debug("метод workWithDbWithoutAnswer() успешно отработал");
    }

    private String getSqlCommandForCreate(String[] params) {
        StringBuilder sqlRequest = new StringBuilder("CREATE TABLE " + params[1] + " (id SERIAL, ");
        for (int i = 2; i < params.length; i++) {
            sqlRequest = sqlRequest.append(params[i]).append(" VARCHAR(255), ");
        }
        sqlRequest = sqlRequest.append("PRIMARY KEY (id))");
        return sqlRequest.toString();
    }

    @Override
    public List<String> tables() throws UnknowShitException {
        ArrayList<String> tablenames = new ArrayList<>();

        DatabaseMetaData databaseMetaData;
        ResultSet resultSet = null;

        try {
            if (connectionToDatabase == null) {
                logger.debug("Команда была вызвана без предварительного подключения к базе.\n" +
                        "Ошибка корректно обработана");
                throw new UnknowShitException("Вы попытались очистить таблицу, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "'connect|database|username|password'");
            }
            databaseMetaData = connectionToDatabase.getMetaData();
            logger.debug("Получена метадата из коннекшена connectionToDatabase");
            resultSet = databaseMetaData.getTables(null, null, "%",
                    new String[]{"TABLE"});
            logger.debug("Из метадаты достаем имена таблиц");
            while (resultSet.next()) {
                tablenames.add(resultSet.getString("TABLE_NAME"));
            }
            if (tablenames.size() > 0) {
                logger.debug(String.format("сформирован ответ с перечнем имен таблиц в следующем виде:\n%s",
                        tablenames.toString()));
                return tablenames;
            } else {
                logger.debug("Сформирован ответ об отсутствии таблиц в БД");
                throw new UnknowShitException("В базе данных нет ни одной таблицы");
            }
        } catch (SQLException a) {
            logger.error(String.format("Неизвестное SQL-Exception. Причина:\n%s", a.getStackTrace()));
            throw new UnknowShitException(String.format("ошибка в работе с Базой данныхю причина: %s",a.getMessage()));
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                logger.debug("resultSet закрыт");
            } catch (SQLException e) {
                logger.error("resultSet не удалось закрыть!!!");
            }
        }
    }

    @Override
    public void clear(String[] params) throws UnknowShitException {
        String sqlRequest = "DELETE FROM " + params[1];
        logger.debug(String.format("Составлен запрос в БД:\n%s", sqlRequest));
        workWithDbWithoutAnswer(sqlRequest);
        logger.debug("Метод workWithDbWithoutAnswer отработал корректно");
    }

    @Override
    public void drop(String[] params) throws UnknowShitException {
        String sqlRequest = "DROP TABLE ".concat(params[1]);
        logger.debug(String.format("Составлен запрос в БД:\n%s", sqlRequest));
        workWithDbWithoutAnswer(sqlRequest);
        logger.debug("Метод workWithDbWithoutAnswer отработал корректно");

    }

    @Override
    public void exit() throws UnknowShitException {
        if (connectionToDatabase != null) {
            try {
                connectionToDatabase.close();
                logger.debug("connectionToDatabase успешно закрыт");
            } catch (SQLException e) {
                logger.error(String.format("Зафейлено закрытие connectionToDatabase. Причина:\n%s",
                        e.getStackTrace()));
                throw new UnknowShitException(String.format("Не удалось закрыть подключение к базе.\n" +
                        "Причина: %s", e.getMessage()));
            }
        }
    }

    @Override
    public void insert(String[] commandFromUser) throws UnknowShitException {
        List<String> columnValues = getValuesForSqlCommand(commandFromUser);
        List<String> columnNames = getNamesForSqlCommand(commandFromUser);
        String sqlCommand = getSqlCommand(commandFromUser[1], columnValues, columnNames);
        logger.debug(String.format("Сформирован запрос в БД:\n%s", sqlCommand));
        workWithDbWithoutAnswer(sqlCommand);
        logger.debug("метод workWithDbWithoutAnswer отработал корректно");

    }

    private List<String> getNamesForSqlCommand(String[] params) {
        List<String> columnNames = new ArrayList<>();
        for (int indexOfcommandFromUser = 2; indexOfcommandFromUser < params.length; indexOfcommandFromUser++) {
            if (indexOfcommandFromUser % 2 == 0) {
                columnNames.add(params[indexOfcommandFromUser].trim());
            }
        }
        return columnNames;
    }

    private List<String> getValuesForSqlCommand(String[] params) {
        List<String> columnValues = new ArrayList<>();
        for (int indexOfcommandFromUser = 3; indexOfcommandFromUser < params.length; indexOfcommandFromUser++) {
            if (indexOfcommandFromUser % 2 == 1) {
                columnValues.add(params[indexOfcommandFromUser].trim());
            }
        }
        return columnValues;
    }

    private String getSqlCommand(String param, List<String> columnValues, List<String> columnNames) {
        StringBuilder result = new StringBuilder("INSERT INTO ");
        result.append(param).append(" (");
        for (int index = 0; index < columnNames.size(); index++) {
            if (index != columnNames.size() - 1)
                result.append(columnNames.get(index)).append(", ");
            else
                result.append(columnNames.get(index));
        }
        result.append(")VALUES ('");
        for (int index = 0; index < columnValues.size(); index++) {
            if (index != columnNames.size() - 1)
                result.append(columnValues.get(index)).append("', '");
            else
                result.append(columnValues.get(index)).append("'");
        }
        result.append(")");
        return result.toString();
    }

    @Override
    public void update(String[] commandFromUser) throws UnknowShitException {
        String command = getSqlCommandForUpdate(commandFromUser);
        logger.debug(String.format("Сформирован запрос в БД:\n%s", command));

        workWithDbWithoutAnswer(command);
        logger.debug("метод workWithDbWithoutAnswer отработал корректно");
    }

    private String getSqlCommandForUpdate(String[] commandFromUser) {
        StringBuilder commandForSql = new StringBuilder("UPDATE ").append(commandFromUser[1]).append(" SET ");
        for (int indexOfCommand = 4; indexOfCommand < commandFromUser.length; indexOfCommand++) {
            if (indexOfCommand % 2 == 0)
                commandForSql.append(commandFromUser[indexOfCommand] + " = '");
            else
                commandForSql.append(commandFromUser[indexOfCommand] + "', ");
        }
        commandForSql.setLength(commandForSql.length() - 2);
        commandForSql.append(" WHERE ").append(commandFromUser[2]).append(" ='" + commandFromUser[3] + "'");
        return commandForSql.toString();
    }

    @Override
    public void delete(String[] params) throws UnknowShitException {
        String sqlForWork = String.format("DELETE FROM %s WHERE %s ='%s'", params[1], params[2], params[3]);
        logger.debug(String.format("Сформирован запрос в БД:\n%s", sqlForWork));
        workWithDbWithoutAnswer(sqlForWork);
        logger.debug("метод workWithDbWithoutAnswer отработал корректно");
    }


    private void workWithDbWithoutAnswer(String sqlRequest) throws UnknowShitException {

        if (connectionToDatabase == null) {
            logger.warn("Попытка входа в базу без предварительного подключения.\nСоздан новый UnknowShitException");
            throw new UnknowShitException("Вы попытались выполнить работу, не подключившись к базе данных.\n" +
                    "Подключитесь к базе данных командой\n" +
                    "'connect|database|username|password'");
        } else {
            try (Statement statement = connectionToDatabase.createStatement()) {
                statement.execute(sqlRequest);
                logger.debug("statement отработал корректно");
            } catch (SQLException e) {
                logger.warn(String.format("получено SQL-Exception:\n%s", e.getStackTrace()));
                throw new UnknowShitException(String.format("Ошибка в работе с базой данных. Причина: %s", e.getMessage()));
            }
        }
    }

    @Override
    public List<String> getColumnNameForFind(String[] command) throws UnknowShitException {
        List<String> responceWithColumnNames;
        String sqlRequest = String.format("SELECT * FROM %s", command[1]);
        logger.debug(String.format("Создан SQL-запрос для получения имен колонок из ДБ:\n%s", sqlRequest));
        responceWithColumnNames = getColumnNamesFromDB(sqlRequest);
        logger.debug(String.format("Получены имена колонок таблицы:\n%s", responceWithColumnNames.toArray().toString()));

        return responceWithColumnNames;
    }

    @Override
    public List<String> getColumnValuesForFind(String[] command) throws UnknowShitException {
        List<String> responceWithColumnValues;
        String sqlRequest = String.format("SELECT * FROM %s", command[1]);
        logger.debug(String.format("Создан SQL-запрос для получения значений колонок из ДБ:\n%s", sqlRequest));
        responceWithColumnValues = getColumnValuesFromDB(sqlRequest);
        logger.debug(String.format("Получены значения колонок таблицы:\n%s",
                responceWithColumnValues.toArray().toString()));

        return responceWithColumnValues;
    }

    @Override
    public List<String> getColumnNameForUpdateOrDelete(String[] command) throws UnknowShitException {
        List<String> columnNames;
        String sqlRequest = String.format("SELECT * FROM %s WHERE %s ='%s'", command[1], command[2], command[3]);
        logger.debug(String.format("Создан SQL-запрос для получения имен колонок из ДБ:\n%s", sqlRequest));
        columnNames = getColumnNamesFromDB(sqlRequest);
        logger.debug(String.format("Получены имена колонок таблицы:\n%s", columnNames.toArray().toString()));
        return columnNames;
    }

    @Override
    public List<String> getColumnValuesForUpdateOrDelete(String[] command) throws UnknowShitException {
        List<String> columnValues;
        String sqlRequest = String.format("SELECT * FROM %s WHERE %s ='%s'", command[1], command[2], command[3]);
        logger.debug(String.format("Создан SQL-запрос для получения значений колонок из ДБ:\n%s", sqlRequest));
        columnValues = getColumnValuesFromDB(sqlRequest);
        logger.debug(String.format("Получены значения колонок таблицы:\n%s",
                columnValues.toArray().toString()));
        return columnValues;
    }


    private List<String> getColumnNamesFromDB(String responceToDB) throws UnknowShitException {

        List<String> responceWithColumnNames = new ArrayList<>();
        if (connectionToDatabase == null) {
            logger.warn("Была попытка выполнить операцию без предварительного подключения к базе.\n" +
                    "Выброшен новый UnknowShitException");
            throw new UnknowShitException("Вы попытались выполнить работу, не подключившись к базе данных.\n" +
                    "Подключитесь к базе данных командой\n" +
                    "'connect|database|username|password'");
        }
        try (Statement statement = connectionToDatabase.createStatement();
             ResultSet resultSet = statement.executeQuery(responceToDB)) {
            logger.info("Были созданы новые statement и resultSet");
            ResultSetMetaData rsmd = resultSet.getMetaData();
            logger.debug("Получена метаДата из resultSet");
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++)
                responceWithColumnNames.add(rsmd.getColumnName(i));
            logger.debug(String.format("Получены имена колонок таблицы:\n%s",
                    responceWithColumnNames.toArray().toString()));

        } catch (SQLException a) {
            logger.warn(String.format("Ошибка в работе с базой данных. Причина: %s", a.getStackTrace()));
            throw new UnknowShitException(String.format("Ошибка в работе с базой данных. Причина: %s",a.getMessage()));
        }
        return responceWithColumnNames;
    }


    private List<String> getColumnValuesFromDB(String responceToDB) throws UnknowShitException {
        if (connectionToDatabase == null) {
            logger.warn("Была попытка выполнить операцию без предварительного подключения к базе.\n" +
                    "Выброшен новый UnknowShitException");
            throw new UnknowShitException("Вы попытались выполнить работу, не подключившись к базе данных.\n" +
                    "Подключитесь к базе данных командой\n" +
                    "'connect|database|username|password'");
        }
        ArrayList<String> responceWithColumnValues = new ArrayList<>();
        try (Statement statement = connectionToDatabase.createStatement();
             ResultSet resultSet = statement.executeQuery(responceToDB)) {
            logger.info("Были созданы новые statement и resultSet");
            ResultSetMetaData rsmd = resultSet.getMetaData();
            logger.debug("Получена метаДата из resultSet");
            int columnCount = rsmd.getColumnCount();
            while (resultSet.next()) {
                for (int index = 1; index <= columnCount; index++)
                    responceWithColumnValues.add(resultSet.getString(index));
                logger.debug(String.format("Получены имена колонок таблицы:\n%s",
                        responceWithColumnValues.toArray().toString()));
            }
        } catch (SQLException a) {
            logger.warn("Была попытка выполнить операцию без предварительного подключения к базе.\n" +
                    "Выброшен новый UnknowShitException");
            throw new UnknowShitException(String.format("Ошибка в работе с базой данных. Причина: %s",a.getMessage()));
        }
        return responceWithColumnValues;
    }
}

