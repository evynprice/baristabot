---
layout: single
permalink: /docs/self-hosting/
header:
  overlay_color: "#FFA500"
sidebar:
  nav: "docs"
toc: true
title: Self Hosting

---
# Getting Started

First, create a new bot user by heading over to [https://discord.com/developers/](https://discord.com/developers/) and signing in with your Discord account. Create a new application, give it a name, and then click on "Bot" on the left side of the panel.

[![Discord Developer Applications Portal](/images/self-hosting/applications-portal.png)](/images/self-hosting/applications-portal.png)

[![Discord Developer New Application](/images/self-hosting/new-application.png)](/images/self-hosting/new-application.png)

Then, give the bot a name and click the "copy" button under Token. This is your API token, and will be used later in the bot configuration. **Never reveal your API token to anyone!**

**Note:** Server Members Intent must be checked for activity logs to function properly.
{: .notice--info}

[![Discord Developer Bot Application](/images/self-hosting/bot-application.png)](/images/self-hosting/bot-application.png)

# Docker configuration
[DockerHub Repository](https://hub.docker.com/repository/docker/evynprice/baristabot)

Barista Bot currently supports both SQLite and MySQL databases. MySQL is recommended for production and docker instances of the bot.

### Required Environment Variables

| Key | Example Value | 
|----|----|
| BOT_TOKEN | yourToken |
| BOT_PREFIX | b. |
| BOT_ADMINID | yourDiscordUserId
| DATABASE | MYSQL |
| DB_URL | db:3306/yourDatabase |
| DB_USER | databaseUser |
| DB_PASS | databasePassword |

### Example Docker-Compose file
```yml
version: "2"

services:
  db:
    image: mysql:5.7
    restart: unless-stopped
    environment:
      MYSQL_ROOT_PASSWORD: pass
      MYSQL_DATABASE: barista_db
      MYSQL_USER: barista
      MYSQL_PASSWORD: pass
      
  barista_client:
   image: evynprice/baristabot:latest
   restart: unless-stopped
   environment:
     BOT_TOKEN: yourToken
     BOT_PREFIX: b.
     BOT_ADMINID: 214045916246573057
     DATABASE: MYSQL
     DB_URL: db:3306/barista_db
     DB_USER: barista
     DB_PASS: pass
```
​
## .jar configuration

Download the latest artifact from [Jenkins](https://ci.evyn.me/job/baristabot/) and move it into a new folder

Create a new file `.env.local` in that directory. Copy the contents from [the sample file](https://github.com/evynprice/baristabot/blob/main/.env.local.sample) in the GitHub repository and paste them into this new file

Fill in the required fields. 

**DATABASE**, **DB_URL**, **DB_USER**, and **DB_PASS** are only required if you are using a MySQL Database.
{: .notice--info}

### Example Config File

```
BOT_TOKEN=yourToken
BOT_PREFIX=b.
BOT_ELEVATED=yourUserId
DATABASE=MYSQL (Optional)
DB_URL=localhost:3306/baristabot (Optional)
DB_USER=barista (Optional)
DB_PASS=password (Optional)
```

Save the env configuration file.

In the bot directory, run the command `java -jar {jar-name}.jar`