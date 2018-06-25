Packet
===
This Document describes the network protocol used by Dance! Online.

Table of contents
===
- [Endianness](#endianness)
- [Value Types](#value-types)
- [Custom Types](#custom-types)
  - [LOGIN_MESSAGE](#loginmessage)
- [Packet Structure](#packet-structure)
- [Packet Header](#packet-header)
- [Login](#login)
  - [1000](#1000) - [LOGIN_REQUEST_AUTHENTICATION](#loginrequestauthentication)
  - [1001](#1001) - [LOGIN_RESPONSE_AUTHENTICATION](#loginresponseauthentication)
  - [1002](#1002) - [LOGIN_REQUEST_CHANNEL_LIST](#loginrequestchannellist)
  - [1003](#1003) - [LOGIN_RESPONSE_CHANNEL_LIST](#loginresponsechannellist)
  - [1004](#1004)
  - [1005](#1005)
  - [1007](#1007)
  - [1008](#1008)
  - [1009](#1009)
  - [1010](#1010)
  - [1011](#1011)
  - [1012](#1012)
  - [1015](#1015)
  - [1016](#1016)
- [Lobby](#lobby)
- [Competition](#competition)
- [Room](#room)
- [Gamble](#gamble)
- [Game](#game)
- [Shop](#shop)
- [Mail](#mail)
- [Wedding](#wedding)
- [Item](#item)
- [Group](#group)
- [Links](#links)

Endianness
===
The order of bytes is little-endian, that means the least significant byte is received/send first.
The following example shows how to interpret a 2byte or 4byte little-endian hex-value:
2bytes:
```
dc 05 -> 05 dc - convert little-endian to big-endian
05 dc -> 5dc   - striped zeros for readability
5dc   -> 1500  - convert to decimal value
```
4bytes:
```
dc 05 00 00 -> 00 00 05 dc - convert little-endian to big-endian
00 00 05 dc -> 5dc         - striped zeros for readability
5dc         -> 1500        - convert to decimal value
```
Converting the little-endian hex value of 'dc050000' directly will result in a decimal value of '3691315200'.
This yields the wrong result, so it is important to understand and interpret the bytes correctly when analysing packets.
When a packet needs to be send, the process needs to be reserved. 
The bytes need to be written as little-endian. 
The following example illustrates the conversion:
2bytes:
```
1500  -> 5dc   - convert decimal- to hex-value
5dc   -> 05 dc - added zeros for readability
05 dc -> dc 05 - convert to little-endian
```
4bytes:
```
1500        -> 5dc         - convert decimal- to hex-value
5dc         -> 00 00 05 dc - added zeros for readability
00 00 05 dc -> dc 05 00 00 - convert to little-endian
```
This does not apply to strings because each character is represented as single byte.
Only values who are represented in multiple bytes are affected by endianness.

Value Types
===
A list of value types used by the protocol:
|Type|Size|Description|Example Value|Example HEX|
|---|---|---|---|---|
|BYTE|1|byte value|10|0A|
|BOOL|1|boolean value|1|01|
|INT16|2|two byte integer value|1500|dc 05|
|INT32|4|four byte integer value|1500|dc 05 00 00|
|FLOAT|4|four byte float value|123.45|TODO|
|XSTRING|X|variable byte string, terminated with 1 nul-byte|scorppio88|63 6f 72 70 70 69 6f 38 38 00|
|STRING|5|fixed byte string|scorp|63 6f 72 70 69|

Custom Types
===
A list of custom types used by the protocol:

## LOGIN_MESSAGE
4 Bytes / INT32
|HEX Value|Message|
|---|---|
|0x0|No Error/Message|
|0xFFFFFFFF|Login Error|
|0xFFFFFFFB|Old Client|
|0xFFFFFFFC|Account Banned|
|0xFFFFFFFD|Already Online|
|0xFFFFFFFE|Wrong Password|

Packet Structure
===
|Part|Size|
|---|---|
|HEADER|4|
|CONTENT|X|
|END|1|
Every packet starts with a 4byte header, followed by a variable content length. 
Many packets end with a null byte (00), but this is not confirmed and needs more observation.
For now refer to packet logs whenever available to check whether it ends with a null byte.

Packet Header
===
All packets start with a header of 4 bytes.
|Size|Type|Name|Description|
|---|---|---|---|
|2|INT16|Length|Number representing the total bytes belonging to this package|
|2|INT16|Id|Packet identification number|

Login
===
These packets are mostly related to the login process.

## 1000
## LOGIN_REQUEST_AUTHENTICATION
LOGIN_REQUEST_AUTHENTICATION
|Size|Type|Name|Description|
|---|---|---|---|
|2|INT16|Packet Length||
|2|INT16|Packet Id||
|X|XSTRING|User Name||
|32|STRING|MD5 Password Hash||
|2|INT16|Major Version||
|2|INT16|Minor Version||
|1|BYTE|END||
Sample
```
34 00 e8 03 73 63 6f 72  70 70 69 6f 38 38 00 38   4...scor ppio88.8
37 33 31 38 32 64 37 32  63 30 32 31 38 36 34 34   73182d72 c0218644
37 64 32 37 33 31 65 61  32 62 61 36 33 35 38 05   7d2731ea 2ba6358.
00 33 00 00                                        .3..
```

## 1001
## LOGIN_RESPONSE_AUTHENTICATION
|Size|Type|Name|Description|
|---|---|---|---|
|2|INT16|Packet Length||
|2|INT16|Packet Id||
|4|[LOGIN_MESSAGE](#loginmessage)|Login Message||
|2|INT16|Major Version||
|2|INT16|Minor Version||
|1|BYTE|||
|1|BOOL|New Character||
|1|BYTE|||
|X|XSTRING|User Name||
|1|BYTE|||
|1|BYTE|END||
Sample
```
18 00 e9 03 00 00 00 00  05 00 33 00 00 00 01 63   ........ ..3....c
61 74 61 38 38 00 01 00                            ata88... 
```

## 1002
## LOGIN_REQUEST_CHANNEL_LIST
|Size|Type|Name|Description|
|---|---|---|---|
|2|INT16|Packet Length||
|2|INT16|Packet Id||

## 1003
## LOGIN_RESPONSE_CHANNEL_LIST
|Size|Type|Name|Description|
|---|---|---|---|
|2|INT16|Packet Length||
|2|INT16|Packet Id||
|4|INT32|Channel Count|Number of channels in the packet|
|1|BYTE|Remaining Packet Count|Rendered when the packet with number 0 is received ?NEEDS TESTING?|
|REPEAT FOR CHANNEL|
|2|INT16|Channel Type|The Tab of the Channel|
|2|INT16|Channel Position|The position inside the Tab|
|X|XSTRING|Channel Name||
|4|INT32|Max Users||
|4|INT32|Current Users||
|4|INT32|Unknown||
|4|INT32|Unknown||
|END REPEAT CHANNEL|
|1|BYTE|END||
Sample
```
Packet 1)
50 01 eb 03 0a 00 00 00  01 00 00 00 00 43 6c 75   P....... .....Clu
62 20 4f 7a 6f 6e 65 00  96 00 00 00 4b 00 00 00   b Ozone. ....K...
01 00 00 00 01 00 00 00  00 00 01 00 43 6c 75 62   ........ ....Club
20 46 6f 63 75 73 00 96  00 00 00 00 00 00 00 01    Focus.. ........
00 00 00 01 00 00 00 00  00 02 00 43 6c 75 62 20   ........ ...Club 
56 6f 6f 64 6f 6f 00 96  00 00 00 00 00 00 00 01   Voodoo.. ........
00 00 00 01 00 00 00 00  00 03 00 43 6c 75 62 20   ........ ...Club 
50 75 6c 73 61 72 00 96  00 00 00 01 00 00 00 01   Pulsar.. ........
00 00 00 01 00 00 00 00  00 04 00 43 6c 75 62 20   ........ ...Club 
56 65 6c 76 65 74 00 96  00 00 00 01 00 00 00 01   Velvet.. ........
00 00 00 01 00 00 00 01  00 00 00 53 74 75 64 69   ........ ...Studi
6f 20 4f 7a 6f 6e 65 00  96 00 00 00 5d 00 00 00   o Ozone. ....]...
01 00 00 00 01 00 00 00  01 00 01 00 53 74 75 64   ........ ....Stud
69 6f 20 46 6f 63 75 73  00 96 00 00 00 15 00 00   io Focus ........
00 01 00 00 00 01 00 00  00 01 00 02 00 53 74 75   ........ .....Stu
64 69 6f 20 56 6f 6f 64  6f 6f 00 96 00 00 00 04   dio Vood oo......
00 00 00 01 00 00 00 01  00 00 00 01 00 03 00 53   ........ .......S
74 75 64 69 6f 20 50 75  6c 73 61 72 00 96 00 00   tudio Pu lsar....
00 01 00 00 00 01 00 00  00 01 00 00 00 01 00 04   ........ ........
00 53 74 75 64 69 6f 20  56 65 6c 76 65 74 00 96   .Studio  Velvet..
00 00 00 09 00 00 00 01  00 00 00 01 00 00 00 00   ........ ........

Packet 2)
a5 00 eb 03 05 00 00 00  00 02 00 00 00 46 72 65   ........ .....Fre
65 74 61 6c 6b 20 4f 7a  6f 6e 65 00 96 00 00 00   etalk Oz one.....
00 00 00 00 01 00 00 00  01 00 00 00 02 00 01 00   ........ ........
46 72 65 65 74 61 6c 6b  20 46 6f 63 75 73 00 96   Freetalk  Focus..
00 00 00 00 00 00 00 01  00 00 00 01 00 00 00 05   ........ ........
00 00 00 54 6f 75 72 6e  61 6d 65 6e 74 31 00 96   ...Tourn ament1..
00 00 00 00 00 00 00 01  00 00 00 01 00 00 00 05   ........ ........
00 01 00 54 6f 75 72 6e  61 6d 65 6e 74 32 00 96   ...Tourn ament2..
00 00 00 00 00 00 00 01  00 00 00 01 00 00 00 09   ........ ........
00 00 00 00 96 00 00 00  00 00 00 00 01 00 00 00   ........ ........
01 00 00 00 00                                     .....
```

## 1004
LOGIN_REQUEST_ENTER_LOBBY_FROM_CHANNEL_SELECTION
## 1005
LOGIN_RESPONSE_ENTER_LOBBY_FROM_CHANNEL_SELECTION
## 1007
LOGIN_REQUEST_CONTROLLER
## 1008
LOGIN_RESPONSE_CONTROLLER
## 1009
LOGIN_REQUEST_ENTER_CHANNEL_SELECTION_FROM_LOBBY
## 1010
LOGIN_RESPONSE_ENTER_CHANNEL_SELECTION_FROM_LOBBY
## 1011
LOGIN_REQUEST_DANCE_LESSON
## 1012
LOGIN_RESPONSE_DANCE_LESSON
## 1015
LOGIN_REQUEST_CREATE_CHARACTER
## 1016
LOGIN_RESPONSE_CREATE_CHARACTER

Lobby
===


Competition
===


Room
===


Gamble
===


Game
===


Shop
===


Mail
===


Wedding
===


Item
===


Group
===


Links
===

Links
===
- [https://en.wikipedia.org/wiki/Endianness](https://en.wikipedia.org/wiki/Endianness)
