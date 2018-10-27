package ua.com.juja.model;

import ua.com.juja.model.newExceptions.NullableAnswerException;
import ua.com.juja.model.parentClassesAndInterfaces.AbstractCreateRequestSenderAndResponseGetter;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewImpl;
import ua.com.juja.view.ViewInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModelImplWithPostgre extends AbstractCreateRequestSenderAndResponseGetter implements ModelInterface {
    private Connection connectionToDatabase;
    private ViewInterface view = new ViewImpl();

    public ModelImplWithPostgre() {
    }

    //готово
    public void сonnect(String[] params) {
        String url = "jdbc:postgresql://localhost:5432/" + params[1];
        String user = params[2];
        String password = params[3];
        String jdbcDriver = "org.postgresql.Driver";

        connectionToDatabase = returnConnection(url, user, password, jdbcDriver);

    }

    //готово
    public void create(String[] params) {
        StringBuilder sqlRequest = new StringBuilder("CREATE TABLE IF NOT EXISTS " + params[1] + " (id SERIAL, ");
        for (int i = 2; i < params.length; i++) {
            sqlRequest = sqlRequest.append(params[i]).append(" VARCHAR(255), ");
        }
        sqlRequest = sqlRequest.append("PRIMARY KEY (id))");
        try {
            requestWithoutAnswer(connectionToDatabase,sqlRequest.toString());
            view.write("Таблица '" + params[1] + "' успешно создана");
        } catch (SQLException e) {
            view.write("Неизвестная ошибка. Обратитесь с возникшей проблемой к разработчику");
            e.printStackTrace();
        } catch (NullPointerException e) {
            view.write("Вы попытались создать таблицу, не подключившись к базе данных.\n" +
                    "Подключитесь к базе данных командой\n" +
                    "connect|database|username|password");
        }
    }

    //готово))
    public void tables() {

        List<String>tablenames = new ArrayList<String>();
        DatabaseMetaData databaseMetaData = null;
        try {
            databaseMetaData = connectionToDatabase.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null,null,"%",
                    new String[]{"TABLE"});
            while (resultSet.next()){
                System.out.println(resultSet.getString("TABLE_NAME"));
                tablenames.add(resultSet.getString("TABLE_NAME"));
            }
            view.write(tablenames.toString());
            resultSet.close();
        } catch (SQLException e) {
            view.write("Возникли проблемы в методе Tables. " +
                    "Поверьте правильно ли вы подключились к базе");
        } catch (NullPointerException e1){
            view.write("Вы попытались получить список таблиц, не подключившись к базе данных.\n" +
                    "Подключитесь к базе данных командой\n" +
                    "connect|database|username|password");
        } catch (IndexOutOfBoundsException e3){
            view.write("На данный момент в базе данных нет ни одной таблицы");
        }
    }

    //готово
    public void clear(String[] params) {
        String sqlRequest = "DELETE FROM " + params[1];
        try {
            requestWithoutAnswer(connectionToDatabase,sqlRequest.toString());
            view.write("Все данные из таблицы ".concat(params[1])
                    .concat(" были удалены"));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e1){
            view.write("Вы попытались очистить таблицу, не подключившись к базе данных.\n" +
                    "Подключитесь к базе данных командой\n" +
                    "'connect|database|username|password''");
        }

    }

    //готово
    public void drop(String[] params) {
        String sqlRequest = "DROP TABLE ".concat(params[1]);
        try {
            requestWithoutAnswer(connectionToDatabase,sqlRequest);
            view.write("Таблица " + params[1] + " успешно удалена");
        } catch (SQLException e) {
            view.write("Вы попытались удалить несуществующую таблицу.\n" +
                    "Введите команду 'tables', чтобы увидеть все созданные таблицы");
        } catch (NullPointerException e){
            view.write("Вы попытались удалить таблицу, не подключившись к базе данных.\n" +
                    "Подключитесь к базе данных командой\n" +
                    "connect|database|username|password");
        }

    }

    //ГОТОВО
    public void find(String[] params) {
        //получаем название колонок из указанной таблицы, чтобы позже добавить их в таблицу отображения
        StringBuilder getColumnNamesForResponce = new StringBuilder("SELECT * FROM ");
        getColumnNamesForResponce.append(params[1]);
        //готовим запрос на получение значений колонок, подходящих под условия клиента
        //ИМЕННО В ЭТОМ МЕТОДЕ ИДЕТ ДУБЛЯЖ, Т.К ТУТ МНЕ НУЖНО ВСЕ (ВОЗМОЖНО, ЭТО КОСТЫЛЬ)
        StringBuilder getColumnValuesForResponce = getColumnNamesForResponce;
        try {
            view.write(requestWithAnswer(connectionToDatabase,getColumnNamesForResponce.toString(),
                    getColumnValuesForResponce.toString(),null));
        } catch (NullableAnswerException s){
          view.write("в данной таблице отсутствует информация");
        } catch (SQLException e) {
            view.write("Ошибка внутри метода 'find'");
        } catch (NullPointerException e1){
            view.write("Вы попытались найти таблицу, не подключившись к базе данных. Сначала подключитесь");
        }

    }
    //готово
    public void insert(String[] params) {
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
            if(index!=columnNames.size()-1)
                mainSqlRequest.append(columnNames.get(index)).append(", ");
            else
                mainSqlRequest.append(columnNames.get(index));
        }
        //добавляем к запросу имена параметров
        mainSqlRequest.append(")VALUES ('");
        for (int index = 0; index < columnValues.size(); index++) {
            //ниже идет такой себе костыль. Чтобы отрефракторить, - лучше разобраться с .substring()
            if(index!=columnNames.size()-1)
                mainSqlRequest.append(columnValues.get(index)).append("', '");
            else
                mainSqlRequest.append(columnValues.get(index)).append("'");
        }
        mainSqlRequest.append(")");

        Statement statement = null;
        try {
            //выполняем запрос
            requestWithoutAnswer(connectionToDatabase,mainSqlRequest.toString());
            view.write("Все данные успешно добавлены");
        } catch (SQLException e) {
            //БЛОК 1 - выясняет, создана ли таблица
            boolean theCurrentTableIsExist = false;
            //Создаю запрос, который зарезалтит имя всех таблиц из базы
            try {
                ResultSet resultSet = statement.executeQuery("SELECT table_name FROM " +
                        "information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'");
                while (resultSet.next()){
                    //Перебор имен. Если найдено искомое имя, "флаг на тру" и выход из цикла, - ничего не происходит
                    //Если же имя не нашли, - флаг трушный и выводим вьюху
                    if(resultSet.getString("table_name").equalsIgnoreCase(params[1])){
                        theCurrentTableIsExist = true;
                        break;
                    }
                }
                resultSet.close();
                statement.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            if(!theCurrentTableIsExist){
                view.write("Таблицы, в которую вы пытаетесь внести изменения не существует.\n" +
                        "Воспользуйтесь командой 'help', чтобы узнать команду для создания таблицы");
            }
            //конец БЛОКА 1
            else {
                //БЛОК 2 - Работает, если таблица есть, но данные введены неправильно
                view.write("Проблемно выполнить запрос из за неверного формата данной команды\n" +
                        "Проверьте правильность введенных:\n" +
                        "Названий колонок\n" +
                        "Типов введенных данных для колонок");
            }
            // Конец БЛОКА 2
            //БЛОК 3 - срабатывает, если нет подключения к базе (переданный в класс коннекшен пустой)
        } catch (NullPointerException e){
            view.write("Вы попытались вставить информацию в таблицу, не подключившись к базе данных.\n" +
                    "Подключитесь к базе данных командой\n" +
                    "connect|database|username|password");
        }
    }
    //переделать Под 2 запроса
    public void update(String[] params) {
        //UPDATE table SET column1 = value1, column2 = value2 ,... WHERE condition;
        //
        StringBuilder sqlReqForTableNames = new StringBuilder("SELECT * FROM ").append(params[1]);
        StringBuilder sqlReqForTableValues = new StringBuilder("SELECT * FROM ").append(params[1]).
                append(" WHERE ").append(params[2]).append(" ='"+params[3]+"'");
        StringBuilder sqlRequestForWork = new StringBuilder("UPDATE ").append(params[1]).append(" SET ");
        for(int index = 4;index<params.length;index++){
            if(index%2==0)
                sqlRequestForWork.append(params[index]+" = '");
            else
                sqlRequestForWork.append(params[index]+"', ");
        }
        sqlRequestForWork.setLength(sqlRequestForWork.length()-2);
        sqlRequestForWork.append(" WHERE ").append(params[2]).append(" ='"+params[3]+"'");
        try {
            view.write("Были изменены следующие строки:\n"
                    //ТРАБЛЫ В ЭТОЙ СТРОКЕ
                    + requestWithAnswer(connectionToDatabase,sqlReqForTableNames.toString(),sqlReqForTableValues.toString(),
                    sqlRequestForWork.toString()));
        } catch (NullableAnswerException a){
            view.write("Операция невыполнима, т.к. в таблице отстутствуют рядки с такими значениями");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e1){
            view.write("Вы попытались обновить данные в таблице, не подключившись к базе данных. Сначала подключитесь");
        }

    }
    //Готово вот ваще
    public void delete(String[] params) {
        //Готовим запрос для вывода названий таблицы
        StringBuilder sqlReqForTableNames = new StringBuilder("SELECT * FROM ").append(params[1]);
        //Готовим запрос для вывода значений в таблице, подходящих под условия
        StringBuilder sqlReqForTableValues = new StringBuilder("SELECT * FROM ").append(params[1]).
                append(" WHERE ").append(params[2]).append(" ='"+params[3]+"'");
        //Готовим запрос для удаления данных, подходящих под условия
        StringBuilder sqlForWork = new StringBuilder("DELETE FROM ".concat(params[1].concat(" WHERE ").
                concat(params[2]).concat(" ='"+params[3]+"'")));
        try {
            view.write("Были удалены следующие строки:\n"
                    + requestWithAnswer(connectionToDatabase,sqlReqForTableNames.toString(),sqlReqForTableValues.toString(),
                    sqlForWork.toString()));
        } catch (NullableAnswerException a){
          view.write("Операция невыполнима, т.к. в таблице отсутствуют рядки с таким значением");
        } catch (SQLException e) {
            view.write("Ошибка в методе 'delete'");
        } catch (NullPointerException e1){
            view.write("Вы попытались удалить информацию из таблицы, не подключившись к базе данных.\n" +
                    "Подключитесь к базе данных командой\n" +
                    "connect|database|username|password");
        }
    }
    //готово
    public void help() {
        view.write("   сonnect\n" +
                "Команда для подключения к соответствующей БД\n" +
                "Формат команды: connect | database | username | password\n" +
                "где: database - имя БД\n" +
                "username -  имя пользователя БД\n" +
                "password - пароль пользователя БД\n" +
                "Формат вывода: текстовое сообщение с результатом выполнения операции\n" +
                "    tables\n" +
                "Команда выводит список всех таблиц\n" +
                "Формат: tables (без параметров)\n" +
                "Формат вывода:\n" +
                "в любом удобном формате\n" +
                "например [table1, table2, table3]\n" +
                "    clear\n" +
                "Команда очищает содержимое указанной (всей) таблицы\n" +
                "Формат: clear | tableName\n" +
                "где tableName - имя очищаемой таблицы\n" +
                "Формат вывода: текстовое сообщение с результатом выполнения операции\n" +
                "    drop\n" +
                "Команда удаляет заданную таблицу\n" +
                "Формат: drop | tableName\n" +
                "где tableName - имя удаляемой таблицы\n" +
                "Формат вывода: текстовое сообщение с результатом выполнения операции\n" +
                "    create\n" +
                "Команда создает новую таблицу с заданными полями\n" +
                "Формат: create | tableName | column1 | column2 | ... | columnN\n" +
                "где: tableName - имя таблицы\n" +
                "column1 - имя первого столбца записи\n" +
                "column2 - имя второго столбца записи\n" +
                "columnN - имя n-го столбца записи\n" +
                "Формат вывода: текстовое сообщение с результатом выполнения операции\n" +
                "    find\n" +
                "Команда для получения содержимого указанной таблицы\n" +
                "Формат: find | tableName\n" +
                "где tableName - имя таблицы\n" +
                "Формат вывода: табличка в консольном формате\n" +
                "+--------+---------+------------------+\n" +
                "+  col1  +  col2   +       col3       +\n" +
                "+--------+---------+------------------+\n" +
                "+  123   +  stiven +     pupkin       +\n" +
                "+  345   +  eva    +     pupkina      +\n" +
                "+--------+---------+------------------+\n" +
                "    insert\n" +
                "Команда для вставки одной строки в заданную таблицу\n" +
                "Формат: insert | tableName | column1 | value1 | column2 | value2 | ... | columnN | valueN\n" +
                "где: tableName - имя таблицы\n" +
                "column1 - имя первого столбца записи\n" +
                "value1 - значение первого столбца записи\n" +
                "column2 - имя второго столбца записи\n" +
                "value2 - значение второго столбца записи\n" +
                "columnN - имя n-го столбца записи\n" +
                "valueN - значение n-го столбца записи\n" +
                "Формат вывода: текстовое сообщение с результатом выполнения операции\n" +
                "    update\n" +
                "Команда обновит запись, установив значение column2 = value2, для которой соблюдается условие column1 = value1\n" +
                "Формат: update | tableName | column1 | value1 | column2 | value2\n" +
                "где: tableName - имя таблицы\n" +
                "column1 - имя столбца записи которое проверяется\n" +
                "value1 - значение которому должен соответствовать столбец column1 для обновляемой записи\n" +
                "ВНИМАНИЕ!!! Занчение должно быть со знаком >, < или =\n" +
                "column2 - имя обновляемого столбца записи\n" +
                "value2 - значение обновляемого столбца записи\n" +
                "columnN - имя n-го обновляемого столбца записи\n" +
                "valueN - значение n-го обновляемого столбца записи\n" +
                "Формат вывода: табличный, как при find со старыми значениями обновленных записей.\n" +
                "    delete\n" +
                "Команда удаляет одну или несколько записей для которых соблюдается условие column = value\n" +
                "Формат: delete | tableName | column | value\n" +
                "где: tableName - имя таблицы\n" +
                "Column - имя столбца записи которое проверяется\n" +
                "value - значение которому должен соответствовать столбец column1 для удаляемой записи\n" +
                "Формат вывода: табличный, как при find со старыми значениями удаляемых записей.\n" +
                "    help\n" +
                "Команда выводит в консоль список всех доступных команд\n" +
                "Формат: help (без параметров)\n" +
                "Формат вывода: текст, описания команд с любым форматированием\n" +
                "    exit\n" +
                "Команда для отключения от БД и выход из приложения\n" +
                "Формат: exit (без параметров)\n" +
                "Формат вывода: текстовое сообщение с результатом выполнения операции\n");
    }
    //готово
    public void exit() {
        if (connectionToDatabase != null) {
            try {
                connectionToDatabase.close();
                view.write("Всего хорошего, до встречи снова))");
            } catch (SQLException e) {
                view.write("Возникли проблемы с выходом из базы в методе 'exit'");
            }
        } else {
            view.write("Всего хорошего, до встречи снова))");
        }
    }

    @Override
    public void wrongCommand() {
        view.write("Вы ввели неизвестную команду.\n" +
                "Вызовите команду 'help'");
    }

    private Connection returnConnection(String url, String user, String password, String jdbcDriver) {

        try {
            Class.forName(jdbcDriver);
            connectionToDatabase = DriverManager.getConnection(url, user, password);
            view.write("База успешно подключена");
            return connectionToDatabase;
        } catch (SQLException e) {
            StringBuilder resultForView = new StringBuilder("Вы ввели: ");
            if(!(url.equalsIgnoreCase("jdbc:postgresql://localhost:5432/jujasqlcmd")))
                resultForView.append("\nНеверную ссылку на базу");
            if(!(user.equalsIgnoreCase("postgres")))
                resultForView.append("\nНеверное имя пользователя");
            if(!(password.equalsIgnoreCase("root")))
                resultForView.append("\nНеверный пароль");
            else {//donothing
            }
            resultForView.append("\nПопробуйте снова:P");
            view.write(resultForView.toString());
        } catch (ClassNotFoundException e) {
            view.write("Не найден драйвер подключения к базе\n" +
                    "Передайте разработчику, чтобы добавил либо джарник в либы, либо депенденс в мавен");
        }
        return null;
    }


}

