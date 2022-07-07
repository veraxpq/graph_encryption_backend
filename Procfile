mvn clean install
heroku ps:scale web=1
web: java -Dserver.port=$PORT $JAVA_OPTS -jar target/graph_encryption-0.1.jar

-Dspring.datasource.url=jdbc:mysql://us-cdbr-east-04.cleardb.com:3306/heroku_00aabd9fbc22340?useUnicode=true?reconnect=true&user=b3aa3fe8a34229&sslmode=require&ssl=true&password=98eda00f&useUnicode=true