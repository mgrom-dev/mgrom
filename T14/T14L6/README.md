## Транзакции, ACID. Временные таблицы, управляющие конструкции, циклы

### Удаление данных, таблиц

Пожалуй, самый страшный сон аналитика — удалить продовскую таблицу. Поэтому будьте всегда аккуратны при использовании следующего скрипта.
```sql
DROP TABLE IF EXISTS table_name
```
Данный скрипт удаляет заданную таблицу. Здесь дополнительно используется условие на проверку существования данной таблицы. Данное условие необходимо, когда вы используете эту команду совместно с другими операциями. Самый распространенный случай, когда вы хотите записать данные в чистую (новую) таблицу. Вы можете дополнительно вначале прописать удаление таблицы, если она существует. Тогда, если вам приходится несколько раз запускать код, то вы будете уверены, что пишите данные в действительно чистую таблицу.

### Удаление данных в таблице

Для удаления данных из таблицы существует две команды (DELETE, TRUNCATE TABLE). Пожалуй самая распространенная процедура удаления DELETE. Функция удаляет строки, при этом выбирает для удаления строки по логике функции SELECT. То есть, чтобы удалить данные из базы, необходимо точно определить их. Приведем пример SQL команды для удаления одной строчки:
```sql
DELETE FROM USERS WHERE ID = 2 LIMIT 1
```
Благодаря этому запросу из таблицы USERS будет удалена одна запись, у которой в столбце ID стоит значение 2. Если такого ID нет в базе, то ничего страшного не произойдет, процедура вернет 0 - количество удаленных строк.

Теперь попробуем удалить сразу диапазон данных. Для этого достаточно составить запрос, в результате выполнения которого вернется несколько строк. Все эти строки будут удалены:
```sql
DELETE FROM USERS WHERE ID >= 5;
```
Этот запрос удалит все строки в таблицы, у которых в столбце ID стоит значение не меньше 5. Если не поставить никакого условия WHERE и лимита LIMIT, то будут удалены абсолютно все строки в таблице:
```sql
DELETE FROM USERS
```
Более быстро эту операцию (удаление всех строк из таблицы) можно выполнить с помощью команды TRUNCATE TABLE
```sql
TRUNCATE TABLE USERS
```
Однако есть ряд особенностей в реализации команды TRUNCATE TABLE, которые следует иметь в виду:
- Операция TRUNCATE не записывает в журнал событий удаление отдельных строк. Вследствие чего не может активировать триггеры.
- После операции TRUNCATE для некоторых СУБД (например, Oracle) следует неявная операция COMMIT. Поэтому удаленные в таблице записи нельзя восстановить операцией ROLLBACK. Но существуют и СУБД, в которых операция TRUNCATE может участвовать в транзакциях, например, PostgreSQL и Microsoft SQL Server.
- Операция DELETE блокирует каждую строку, а TRUNCATE — всю таблицу.
- Операция TRUNCATE не возвращает какого-то осмысленного значения (обычно возвращает 0) в отличие от DELETE, которая возвращает число удаленных строк.
- Операция TRUNCATE в некоторых СУБД (например, MySQL или Microsoft SQL Server), сбрасывает значение счетчиков (для полей с AUTOINCREMENT / IDENTITY). В PostgreSQL для сброса счётчиков необходимо указывать модификатор RESTART IDENTITY.
- В SQLite операция как таковая отсутствует, но есть оптимизация операции DELETE, которая «значительно ускоряет её работу, если отсутствует аргумент WHERE».

### Изменение данных

Функция обновления UPDATE (переводится с английского как ОБНОВИТЬ) довольно часто используется в проектах сайтов. Как и в случае с функцией DELETE, функция обновления не успокоится до тех пор, пока не обновит все поля, которые подходят под условия, если нет лимита на выборку. Поэтому необходимо задавать однозначные условия, чтобы вместо одной строки нечаянно не обновить половину таблицы. Приведем пример использования команды UPDATE:
```sql
UPDATE USERS SET NAME = Мышь WHERE ID = 3 LIMIT 1
```
В этом примере, в таблице USERS будет установлено значение Мышь в столбец NAME у строки, в столбце ID которой стоит значение 3. Можно обновить сразу несколько столбцов у одной записи, передав значения через запятую. Попробуем обновить не только значение в столбце NAME, но и значение в столбце FOOD используя один запрос:
```sql
UPDATE USERS SET NAME = Мышь, FOOD = Сыр WHERE ID = 3 LIMIT 1
```

### Создание таблиц

Оператор CREATE служит для создания объектов базы данных. Разные СУБД работают с разными объектами, но наиболее общими для большинства СУБД будут команды создания таблицы (оператор SQL CREATE TABLE) и создания представления (оператор SQL CREATE VIEW, поговорим ниже).

Синтаксис оператора SQL CREATE TABLE выглядит следующим образом:
```sql
CREATE TABLE table_name (
column_name1 data_type(size),
column_name2 data_type(size),
column_name3 data_type(size)
...)
```
Давайте рассмотрим на примере работу оператора CREANE/ Используя оператор CREATE создадим таблицу Planets содержащую следующие поля: ID (числовой), PlanetName (символьный), Radius (вещественный), SunSeason (вещественный), OpeningYear (числовой), HavingRings (логический), Opener (символьный):
```sql
CREATE TABLE Planets (
ID int NOT NULL AUTO_INCREMENT,
PlanetName varchar(10) NOT NULL,
Radius float,
SunSeason float,
OpeningYear int,
HavingRings bit,
Opener varchar(30)
)
```
В данном коде использовались типы СУБД MS SQL Server. В других СУБД названия типов могут отличаться!

В результате выполнения запроса будет создана таблица с перечисленными полями и их типами. Отметим, что поле ID заполняется автоматически как AUTO_INCREMENT. То есть каждая следующая запись будет иметь номер на 1 больше предыдущего. Соответственно, при добавлении данных в эту таблицу данной значение не передаем. Атрибут NOT NUL для первой и второй колонки означает, что данное поле обязательно должно быть заполнено. И если мы попытаемся записать данные, но не передадим имя планеты в нашем случае, то сервер вернет ошибку. Поле HavingRings имеет тип bit, принимающее 2 значения — 1 или 0 (TRUE, FALSE).

Если у вас уже готовы данные, которые вы хотите залить в новую таблицу, то можно использовать упрощенный способ создания таблицы CREATE TABLE AS. CREATE TABLE AS создаёт таблицу и наполняет её данными, полученными в результате выполнения SELECT. Столбцы этой таблицы получают имена и типы данных в соответствии со столбцами результата SELECT (хотя имена столбцов можно переопределить, добавив явно список новых имён столбцов).

CREATE TABLE AS напоминает создание представления, но на самом деле есть значительная разница: эта команда создаёт новую таблицу и выполняет запрос только раз, чтобы наполнить таблицу начальными данными. Последующие изменения в исходных таблицах запроса в новой таблице отражаться не будут. С представлением, напротив, определяющая его команда SELECT выполняется при каждой выборке из него.
```sql
CREATE TABLE demo2 AS SELECT * FROM demo
```
В данном случае будет создана копия таблицы DEMO.

### Создание представлений

В прошлом параграфе мы разобрали блок создания таблицы. Нужно понимать, что при создании таблицы и внесении данных эти данные фактически хранятся на сервере SQL. От бизнеса могут каждый день приходить различные задания на получение данных. В результате может происходить бесконтрольное создание таблиц и размножение данных. В частности, это также приводит к денормализации базы данных.

Но как тогда передать заказчику объект, в котором “собраны” необходимые данные без непосредственной записи в новую таблицу. Для этого используют представления.

В SQL представление — это виртуальная таблица, основанная на наборе результатов оператора SQL. Представление содержит строки и столбцы, как и настоящая таблица. Поля в представлении — это поля из одной или нескольких реальных таблиц в базе данных. Вы можете добавлять операторы и функции SQL в представление и представлять данные так, как если бы они поступали из одной таблицы. Представление создается с помощью CREATE VIEW оператора. Синтаксис оператора следующий.
```sql
CREATE VIEW view_name AS
SELECT column1, column2, ...
FROM table_name
WHERE condition;
```
Как вы понимаете, что во второй части запроса после ключевого слова AS может стоять запрос любой сложности, с различными объединениями и группировками. Поэтому после того, как вы сделали представление, можно отдать заказчику не сложный многоуровневый запрос, а название представления. И он уже простым Селектом сможет получить нужные данные.

Однако, иногда возникают проблемы. Если ваш запрос, из которого вы делаете представление, очень сложный, обрабатывает большое число данных, то представление может работать очень долго. Поскольку при вызове представления каждый раз обрабатывается этот запрос. В результате, необходимо соблюдать баланс между сложностью запроса и количеством получаемых данных. То есть если ваш сложный запрос возвращает мало данных, то лучше их записать в таблицу, а не создавать представление.

### Создание процедур

Отлично, мы научились удалять таблицы, создавать таблицы и представления, чтобы передать заказчику необходимые данные. Но что делать, если необходимо каждый день запускать большой скрипт. Или нужно обработать очень большой массив данных. Например, как-то мне нужно было обработать подневно чеки за семь лет и получить несколько метрик для каждого дня. Каждый день при этом содержал около 600 тыс чеков. Запустить один скрипт сразу на все чеки не представляется возможным. Элементарно на это не хватит оперативной памяти ни в одной системе. Поэтому здесь можно только в цикле запустить какую-то программу, которая переносит данные. Причем в дальнейшем эту программу можно запускать периодически, чтобы добавить свежие данные в своей таблице. Конечно, что впрямую процесс обработки данных ETL (Extract, Transform, Load). Но ждать когда инженеры данных наладят процесс - это не наши методы.

Давайте познакомимся с хранимыми процедурами - набор инструкций, которые выполняются как единое целое

Начнем с синтаксиса:
```sql
CREATE PROCEDURE имя_процедуры (параметры) AS
begin
операторы
end
```
Параметры это те данные, которые мы будем передавать процедуре при ее вызове, а операторы — это собственно запросы. Давайте напишем свою первую процедуру и убедимся в ее удобстве. Нам постоянно для просмотра содержания таблицы, какие там есть поля, какие характерные значения в этих полях приходится писать простой запрос. При этом желательно в этом запросе не забывать указывать число выводимых строк (TOP 50, например). давайте автоматизируем этот запрос и напишем процедуру, которая будет выглядеть следующим образом.
```sql
CREATE PROCEDURE S_S @TN VARCHAR(50), @N int = 10 -- simple select
AS
BEGIN
DECLARE @query VARCHAR(1000)
set @query = 'SELECT TOP('+ CAST(@N AS VARCHAR(10)) + ') * FROM '+ @TN
EXEC (@query)
END
```
Что мы видим в данном коде. Сначала мы определили Имя переменной S_S (от simple select). Имя достаточно короткое, чтобы можно было его быстро написать. Потом определяем две переменные, которые будут использоваться в нашей процедуре — это имя желаемой таблицы и количество строк для вывода. Внутри самой процедуры у нас выполняется тристроки скрипта. Поскольку мы не можем динамически менять имя таблицы в запросе SELECT, то нам приходится делать переменную @query, в которой будет строковое представление нашего запроса. И затем с помощью команды EXEC мы выполняем наш строковый запрос. Давайте выполним код создания процедуры и убедимся, что в списке процедур появилась наша процедура.

Как видно, у этой процедуры есть обязательный параметр - имя таблицы. И не обязательный параметр — количество выводимых строк. Теперь, чтобы получить представление о таблице необходимо только вызвать эту процедуру и указать имя желаемой таблицы.

### Пример использования

А теперь давайте разберем мой пример с обработкой чеков. У нас с вами есть таблица заказов пользователей. Предположим, что заказов очень много и вам необходимо посчитать какие-то произвольные метрики для этих заказов в разрезе суток. Метрики могут быть сколь угодно сложными. Например, можно посчитать выручку за день, выручку на одного клиента в день и так далее. Для простоты мы будем считать просто дневное количество заказов. Да, действительно мы сейчас можем просто взять группировку по дню заказа и посчитать количество заказов. Но давайте представим, что таких дней очень много и метрика чуть сложнее.

И так для решения нашей задачи пригодится следующая процедура.
```sql
CREATE PROC Small_ETL @start date, @finish date, @to_del BIT= 0
AS
IF OBJECT_ID('[dbo].[Orders_stat]') IS not NULL and @to_del = 1
BEGIN
drop TABLE Orders_stat
end;
IF OBJECT_ID('[dbo].[Orders_stat]') IS NULL
BEGIN
create TABLE Orders_stat (dt date, cnt int)
end;
delete from Orders_stat where dt >= @start and dt < @finish
while @start < @finish
BEGIN
INSERT INTO Orders_stat
SELECT @start as dt, COUNT(*) as cnt
from Orders
where orderdate = @start
SET @start = dateadd(day, 1, @start)
end;
```
Сначала в данной процедуре мы объявляем три переменные, начало и конец временного промежутка, по которому хотим собрать данные, и флаг для удаления таблицы, если мы хотим пересобрать все данные заново. Затем мы проверяем условие на удаление таблицы и ее существование. Нужно заметить, что в MSSQL нет прямой возможности проверить существует таблица или нет. Для этого мы проверяем есть ли у нашей таблицы ID. Если ID существует, то и таблица существует. В результате мы удаляем таблицу, если это было необходимо. В следующем блоке мы создаем новую таблицу, если она не существует. В новой таблице мы прописываем поля, которые нам понадобятся. В данном случае мы хотим видеть два поля: дату и количество заказов. Чтобы избежать дубликатов в нашей таблице мы можем удалить старые записи из таблицы, которые попадают в диапазон наших дат. На практике часто встречается ситуация, когда информация о чеках приходит с запозданием или, например, покупатели оформляют возвраты. Это может приводить к изменению статистик. Допустим вы в среду посчитали метрику за понедельник, а затем в четверг хотите ее пересчитать. тогда вам необходимо удалить уже имеющиеся данные за понедельник.

После этого у нас формируется основная логика нашей процедуры — это цикл манипуляций с данными. Как мы уже договорились, мы будем рассчитывать простую метрику сейчас. Но здесь могут быть запросы любой сложности. В данном случае мы фильтруем нашу таблицу заказов Orders по дате и считаем количество строк/заказов, и добавляем расчет в финальную таблицу. Данная операция идет в цикле WHILE, поэтому необходимо все время переопределять переменную для фильтрации. Я уверен, что все из вас знают основы программирования и использования циклов. Если что, на семинарах это можно проговорить отдельно с преподавателем.

В результате наша процедура направлена на обновление таблицы ежедневными данными о заказах. Ниже приведены способы запуска данной процедуры.
```sql
Small_ETL @start = '2023-05-01', @finish = '2023-12-01'
Small_ETL @start = '2023-06-01',
@finish = '2023-12-01',
@to_del = 1
select count(*) from Orders_stat
```
В первом случае, как мы договорились, мы произведем перерасчет метрик за период с первого мая до первого декабря. При этом таблица не будет удаляться. Во втором случае, мы удаляем начальную таблицу и запишем в новую данные с первого июня до первого декабря. С помощью третьей строки вы можете смотреть результаты работы этой процедуры — сколько строк было записано в таблицу.