rm -fr cla/*
#javac -cp cla:lib/h2-1.3.169.jar -d cla -sourcepath src src/h2t.java
#java -cp cla:lib/h2-1.3.169.jar h2t
javac -cp cla -d cla -sourcepath src src/MyDemo.java
java -cp cla MyDemo
