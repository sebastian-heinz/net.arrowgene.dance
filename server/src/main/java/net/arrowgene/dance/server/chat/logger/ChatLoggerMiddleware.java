/*
 * This file is part of net.arrowgene.dance.
 *
 * net.arrowgene.dance is a server implementation for the game "Dance! Online".
 * Copyright (C) 2013-2018  Sebastian Heinz (github: sebastian-heinz)
 * Copyright (C) 2013-2018  Daniel Neuendorf
 *
 * Github: https://github.com/Arrowgene/net.arrowgene.dance
 * Web: https://arrowgene.net
 *
 * net.arrowgene.dance is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * net.arrowgene.dance is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.arrowgene.dance.server.chat.logger;

import net.arrowgene.dance.server.chat.ChatMessage;
import net.arrowgene.dance.server.chat.ChatMiddleware;
import net.arrowgene.dance.log.Log;
import net.arrowgene.dance.log.LogType;
import net.arrowgene.dance.log.Logger;

/**
 * Logs chat messages
 */
public class ChatLoggerMiddleware implements ChatMiddleware {

  private Logger logger;

  public ChatLoggerMiddleware(Logger logger) {
    this.logger = logger;
  }

  @Override
  public void handleMessage(ChatMessage chatMessage) {
    Log chatLog = this.createChatLog(chatMessage);
    this.logger.writeLog(chatLog);
  }

  private Log createChatLog(ChatMessage chatMessage) {

    String text = "";
    String account = "";

    if (chatMessage.getSender() != null) {
      if (chatMessage.getSender().getAccount() != null) {
        account = chatMessage.getSender().getAccount().getUsername();
      }
      if (chatMessage.getSender().getCharacter() != null) {

        text += chatMessage.getSenderCharacterName();
        switch (chatMessage.getChatType()) {
          case CHANNEL: {
            text += "@";
            text += "[CH#" + chatMessage.getSender().getChannel().getDetails().getPosition() + "]";
            break;
          }
          case ROOM: {
            text += "@";
            text += "[CH#" + chatMessage.getSender().getChannel().getDetails().getPosition() + "]";
            text += "[RM#" + chatMessage.getSender().getRoom().getNumber() + "]";
            break;
          }
          case PRIVATE: {
            text += "@";
            text += "[" + chatMessage.getRecipientCharacterName() + "]";
            break;
          }
          default: {
            break;
          }
        }
        text += ": ";
        text += chatMessage.getMessage();
      }
    }

    return new Log(text, LogType.CHAT, account);
  }

}
