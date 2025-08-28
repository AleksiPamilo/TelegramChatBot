## Telegram Bot
A simple Java Telegram bot built using Maven

## Requirements
Java 17 or newer  
Maven 3.8 or newer

## Setup
1. Clone this repository
2. Create a .env file in the project root:

BOT_TOKEN=Your_Telegram_Bot_Token  
WEATHER_API_KEY=Your_OpenWeatherMap_Api_Key

## Build & Run
1. Open terminal in the project root
2. Build and run the project using Maven:
 
`mvn clean compile exec:java -Dexec.mainClass="dev.pmlo.telegram.chatbot.Main"`

> Note: By default, the exec plugin requires specifying the main class. To avoid passing -Dexec.mainClass each time, you can configure the main class in your pom.xml:
```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.codehaus.mojo</groupId>
      <artifactId>exec-maven-plugin</artifactId>
      <version>3.1.0</version>
      <configuration>
      <mainClass>dev.pmlo.telegram.chatbot.Main</mainClass>
      </configuration>
    </plugin>
  </plugins>
</build>
```
After this, you can simply run:  
`mvn clean compile exec:java`

## Commands
- **/dice** - Heittää nopan (1-6)
- **/coin** - Heittää kolikon (Kruuna/Klaava)
- **/weather** <kaupunki> - Hakee sään annetusta kaupungista, esim. /weather Helsinki
- **/joke** - Kertoo hauskan vitsin
- **/menu** - Näyttää tämän päivän lounaslistan
- **/fact** - Palauttaa satunnaisen faktan
- **/spotify** - Näyttää tällä hetkellä kuunnellun kappaleen Spotifysta
- **/quote** - Palauttaa satunnaisen motivaatiolauseen
- **/quiz** - Palauttaa satunnaisen trivia-kysymyksen ja vastausvaihtoehdot, oikea vastaus spoilerina
- **/movie** - Palauttaa satunnaisen elokuvan
- **/news** - Näyttää ajankohtaisia uutisia

### Notes
1. API keys are loaded from the .env file using dotenv-java (https://github.com/cdimascio/dotenv-java)
2. Make sure the .env file is located in the project root so the bot can access the keys correctly.
3. The project dependencies, including TelegramBots API and dotenv-java, are managed via Maven automatically.
