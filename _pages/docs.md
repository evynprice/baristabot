---
layout: single
permalink: /docs
excerpt: "Command Documentation"
header:
  overlay_color: "#e64d42"
sidebar:
  nav: "docs"

---

() Refers an optional argument

[] Refers to a required argument

## Info commands

|     Command      |                     Description                     |         Aliases          |               Usage              |              Example               |
| ---------------- | --------------------------------------------------- | ------------------------ | -------------------------------- | ---------------------------------- |
| **Commands**     | Provides a list of bot commands                     | commands, cmds           | b.commands                       |                                    |
| **Help**         | Provides information about the bot                  | info, information        | b.help                           |                                    |
| **Invite**       | Provides the bot invite link                        |                          | b.invite                         |                                    |
| **Ping**         | Provides the current bot ping                       |                          | b.ping                           |                                    |
| **Server Info**  | Provides information about the current server       | server, guild, guildinfo | b.serverinfo                     |                                    |
| **Stats**        | Provides technical information on the bot process   | statistics, performance  | b.stats                          |                                    |
| **Support**      | Provides a link to the community Discord server     |                          | b.invite                         |                                    |
| **Usage**        | Provides the proper usage of a specific command     |                          | b.usage [command]                | `b.usage ping`                     |
| **User Info**    | Provides information about you or a specific user   | user                     | b.userinfo [userId/userMention]  | `b.userinfo @TheTechnicalFox#0056` |

## Fun Commands

| Command | Description | Aliases | Usage | Example |
| ------------- | ------------- | ------------- | ------------- | ------------- | 
| Say | Says the provided content and deletes the original message | speak | b.say [content] | `b.say hello there` |
| Prequelmeme | Posts a random image from r/prequelmemes | n/a | b.prequelmeme | `b.prequelmeme` |

## Moderation Commands

| Command | Description | Aliases | Usage | Example |
| ------------- | ------------- | ------------- | ------------- | ------------- | 
| Ban | Bans the provided user and deletes messages sent by user based on days. Optionally add reason | n/a | b.ban [userMention/UserId] (delDays 0-7) (reason) | `b.ban @TheTechnicalFox#0056 5 spam` `b.ban @TheTechnicalFox#0056` |
| Kick | Kicks the provided user with an optional reason | n/a | b.kick [userMention/userId] (reason) | `b.kick @TheTechnicalFox#0056 advertising` |
| Purge | Deletes selected amount of messages in current or mentioned channel | prune, delete, remove | b.purge [0-99] (channel name/reason) (reason) | `b.purge 50 #general spamming`

## Admin

| Command | Description | Aliases | Usage | Example |
| ------------- | ------------- | ------------- | ------------- | ------------- | 
| Settings | Changes bot per-guild settings | settings, config, conf | b.settings [setting] (value) | `b.settings prefix $` |

## Counting

| Command | Description | Usage | Example |
| ------------- | ------------- | ------------- | ------------- |
| Counting | Responds with information about Counting Game | b.counting / b.counting help | `b.counting` / `b.counting help` |
| Top | Sends the top 10 counters in your server | b.counting top | `b.counting top` |
| Channel | Sends or edits the current counting channel | b.counting / b.counting #counting | `b.counting` / `b.counting #counting`

## Settings

| Setting | Description | Usage | Example |
| ------------- | ------------- | ------------- | ------------- |
| Prefix | Views or changes the bot's listening prefix | b.setting prefix [newPrefix] | `b.setting prefix $` |
| Embed | Views or changes if the bot should respond with embed messages | b.setting embed [true/false] | `b.setting embed false` |
| Activity-Logs | Views or changes the activity log channel | b.setting activity-logs (channelId/channelMention) | `b.setting activity-logs #activity-logs` |
| Mod-Logs | Views or changes the moderation log channel | b.setting mod-logs (channelId/channelMention) | `b.setting mod-logs #mod-logs` |

