FROM tomcat:7.0-jre8

ADD . /root/

RUN cp -R /root/teamengine /usr/local/tomcat/webapps/teamengine

RUN cp -R /root/common/*.jar /usr/local/tomcat/lib

ENV JAVA_OPTS="-Xms1024m -Xmx2048m -DTE_BASE=/root/te_base"

CMD ["catalina.sh", "run"]
