# -------- Stage 1: Build WAR using Maven --------
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy Maven files
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copy project source
COPY src ./src

# Build the WAR file
RUN mvn clean package -DskipTests

# -------- Stage 2: Run with Tomcat 10 --------
FROM tomcat:10.1.48-jdk17

# Set working directory for Tomcat
WORKDIR /usr/local/tomcat

# Remove the default ROOT webapp
RUN rm -rf webapps/ROOT

# Copy WAR built from previous stage
COPY --from=build /app/target/ROOT.war webapps/ROOT.war

# ✅ Add MySQL connector (important for database connectivity on Render)
# Render containers don’t include it by default.
ADD https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar lib/

# Expose port 8080 (Render uses this)
EXPOSE 8080

# Start Tomcat server
CMD ["catalina.sh", "run"]
