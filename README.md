Reddit r/place Organizasyonunun Java bazlı yeniden yapımı

# Sunucu varsayılan olarak HTTPS desteklememektedir önerim nginx ile 4567 portunda çalışacak sunucuyu HTTPS ile proxilemenizdir
# Örnek bir nginx proxy için config dosyası


`server {`
`   listen 80;`
`   return 301 https://example.yourdomain.com$request_uri;`
` }`
` `
`server {`
`   listen 443 ssl;`
`   ssl_certificate  /etc/nginx/ssl/cert.crt;`
`   ssl_certificate_key  /etc/nginx/ssl/key.key;`
`   ssl_prefer_server_ciphers on;`
` `
`   location / {`
`        proxy_pass http://localhost:4567;`
` `
`        proxy_set_header        Host $host;`
`        proxy_set_header        X-Real-IP $remote_addr;`
`        proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;`
`        proxy_set_header        X-Forwarded-Proto $scheme;`
`        proxy_set_header Upgrade $http_upgrade;`
`        proxy_set_header Connection "Upgrade";`
`        proxy_http_version 1.1;`
`}`
`}`


## Derlemek ("yapmak")

Şu paketler **derlenecek** sisteminde gereklidir:

* [JDK 16][jdk16]
* [Apache Maven][maven]

Şu paketler **hedef** sisteminde gerekilidir:

* [JRE 16][jdk16]
* [Postgres][postgres]

Derlemek için proje klasöründe `mvn clean package` çalıştırın

Derlenen `pxls*.jar` `target/` klasöründe bulunacak.

## Çalıştırmak

Derlenen `pxls*.jar` dosyasını ve aşşağıdaki gibi yeniden adlandırılıan dosyaları proje klasörüne kopyalayın:
#    [eski adı]       [yeni adı]
* `reference.conf` (`pxls.conf`)
* `roles-reference.conf` (`roles.conf`)
* `palette-reference.conf` (`palette.conf`)

Gereğine göre konfigurasyon dosyalarını düzenleyin. `pxls.conf` Dosyası aşşağıdaki gibiKESİNLİKLE veritabanına erişim için ayarlanmalıdır

* `database.user`
* `database.pass`
* `database.url`
* `host`
* `oauth` (Kimlik doğrulama için gerekli)
    * [Reddit][redditapps], [Google][googleconsole], [Discord][discordapps], [VK][vkapps], and [Tumblr][tumblrapps] are current supported.

`java -jar pxls*.jar` ile çalıştırın

# Notlar

* Sunucu varsayılan olarak `4567` portunda başlayacak
* Konsol Komuylarına bakın [rudimentary console](#console-commands).
* Konfig dosyaları [HOCON][hocon] kullanıyor .
* `board.dat`'ın her 5dk alınan yedekleri `backups/` klasörüne yerleştirilmektedir, programdan ( `CTRL + C`) ile çıkılırken de.


# Konsol Komutları 

Commands can be entered into the running instance through standard input.

`exact |exact| (required description) [optional description] one-or-more... DEFAULT/possible/values {description}`

## Genel

| Command | Arguments | Description |
| --- | --- | --- |
| `reload` || Reloads `pxls.conf` and `roles.conf`, applying _most_ changes immediately. Also reloads the user and faction cache. |
| `save` || Saves the board. |
| `alert` | `[message]` | Alerts every user with the given message (or blank if left empty). |
| `cons` | `[\|authed\|]` | Lists the total (or authenticated) connection count. |
| `users` || Lists all authenticated usernames. |
| `broadcast` | `(message)` | Sends the message in chat. |
| `cf` | `(query)` | Runs the query through the chat filter. |
| `relaodusers` || Reloads the user manager. **Laggy!** |
| `idlecheck` || Runs the user timeout check. |
| `senduserdata` || Broadcasts the non-idle user count in chat. |
| `addnotification` | `(title) (expiry) (body)` | Adds a new notification to the notification panel with the body. `+123` for the expiry means 123 seconds from now. |
| `bp` | `(packet)` | Broadcasts a raw JSON packet to all connections. |
| `up` | `(username) (packet)` | Broadcasts a raw JSON packet to all active connections from the user. |
| `f` | `(faction ID) [delete/tag [new tag]/name [new name]]` | Prints information about the faction or changes the tag or name. |

## Kanvas Yönetimi

| Command | Arguments | Description |
| --- | --- | --- |
| `nuke` | `(x1) (y1) (x2) (y2) (color)` | Replaces all pixels from (`x1`, `y1`) to (`x2`, `y2`) with the specified color index. |
| `replace` | `(x1) (y1) (x2) (y2) (from color) (to color)` | Replaces all pixels from (`x1`, `y1`) to (`x2`, `y2`) matching the `from color` index with the `to color` index. |

## Kullanıcı Yönetimi

| Command | Arguments | Description |
| --- | --- | --- |
| `logins` | `(username) [{service ID}:{service user ID} ...]` | Gets or sets the user's login method(s). |
| `addlogins` | `(username) ({service ID}:{service user ID} ...`) | Adds login method(s) to the user. |
| `removelogins` | `(username) ({service ID}:{service user ID} ...`) | Removes login method(s) from the user. |
| `roles` | `(username) [role ID ...]` | Gets or sets the user's roles. |
| `addroles` | `(username) (role ID ...)` | Adds role(s) to the user. |
| `removeroles` | `(username) (role ID ...)` | Removes role(s) from the user. |
| `ban` | `(username) [reason]` | Bans the user for 24 hours with the given reason. |
| `permaban` | `(username) [reason]` | Permanently bans the user with the given reason. |
| `shadowban` | `(username) [reason]` | Shadowbans the user (making new pixels local) with the given reason. |
| `unban` | `(username) [{revert} TRUE/false] [reason]` | Unbans the user, reverting their pixels and with the given reason. |
| `stack` | `(user) [\|set\| (amount)]` | Gets or sets the user's stacked pixels. User must not be on cooldown before setting. |
| `placementOverride` | `\|list\|/(user) [placeAnyColor/ignoreCooldown/ignorePlacemap] [on/off]` | Lists all placement overrides or turns a placement override on/off for the user. |
| `captchaOverride` | `\|list\|/(user) [on/off]` | Lists all captcha overrides or turns it on/off for the user. |
| `chatban` | `(username) (length) ({purge} true/false) (reason)` | Bans the user from chat for the `length` in seconds, optionally purging all of their messages, with the given reason. |
| `permachatban` | `(username) ({purge} true/false) (reason)` | Permanently bans the user from chat optionally purging all of their messages, with the given reason. |
| `unchatban` | `(username)` | Unbans the user from chat. |
| `chatpurge` | `(username) [amount] [reason]` | Purges up to `amount` chat messages from the user for the given reason. |
| `flagrename` | `(username) [{flag} TRUE/false]` | Flags or unflags the user for rename. |
| `setname\|updateusername` | `(username) (new username)` | Renames the user. |

# Licenses
- This project includes icons from Font Awesome Free 5.9.0 by fontawesome - https://fontawesome.com
    - License: https://fontawesome.com/license/free (Icons: CC BY 4.0, Fonts: SIL OFL 1.1, Code: MIT License)


[place]: https://reddit.com/r/place/
[docker]: https://github.com/aneurinprice/docker-pxls.space
[dockerhub]: https://hub.docker.com/r/m08y/docker-pxls.space
[actions]: https://github.com/pxlsspace/Pxls/actions/workflows/maven.yml
[maven]: https://maven.apache.org/
[java]: https://www.java.com/en/download/linux_manual.jsp
[jdk16]: https://openjdk.java.net/projects/jdk/16/
[postgres]: https://www.postgresql.org/
[hocon]: https://github.com/typesafehub/config/blob/master/HOCON.md
[googleconsole]: https://console.developers.google.com
[redditapps]: https://www.reddit.com/prefs/apps
[discordapps]: https://discord.com/developers/applications/me
[vkapps]: https://vk.com/apps?act=manage
[tumblrapps]: https://www.tumblr.com/oauth/apps
[captcha]: https://www.google.com/recaptcha/admin
