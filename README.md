# Reddit r/place Organizasyonunun Java bazlı yeniden yapımı

Sunucu varsayılan olarak HTTPS desteklememektedir önerim nginx ile 4567 portunda çalışacak sunucuyu HTTPS ile proxilemenizdir
Örnek bir nginx proxy için config dosyası


`server {`<br />
`   listen 80;`<br />
`   return 301 https://example.yourdomain.com$request_uri;`<br />
` }`<br />
` `<br />
`server {`<br />
`   listen 443 ssl;`<br />
`   ssl_certificate  /etc/nginx/ssl/cert.crt;`<br />
`   ssl_certificate_key  /etc/nginx/ssl/key.key;`<br />
`   ssl_prefer_server_ciphers on;`<br />
` `<br />
`   location / {`<br />
`        proxy_pass http://localhost:4567;`<br />
` `<br />
`        proxy_set_header        Host $host;`<br />
`        proxy_set_header        X-Real-IP $remote_addr;`<br />
`        proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;`<br />
`        proxy_set_header        X-Forwarded-Proto $scheme;`<br />
`        proxy_set_header Upgrade $http_upgrade;`<br />
`        proxy_set_header Connection "Upgrade";`<br />
`        proxy_http_version 1.1;`<br />
`}`<br />
`}`<br />


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
   [eski adı]       [yeni adı]
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

Çalışan programa komutlar direkten girilebilir

`exact |exact| (required description) [optional description] one-or-more... DEFAULT/possible/values {description}`

## Genel

| Komut | Argüman | Açıklama |
| --- | --- | --- |
| `reload` || `pxls.conf` and `roles.conf`, Dosyalarını yeniden yükler. |
| `save` || Save alır. |
| `alert` | `[message]` | Kullanıcıları verilen mesajla uyarır. |
| `cons` | `[\|authed\|]` | Toplamdaki kullanıcı sayısını verir. |
| `users` || Bütün kimlik doğrulamış kullanıcıları verir |
| `broadcast` | `(message)` | Chat bölümünden mesaj gönderir |
| `relaodusers` || Kullanıcı yöneticisini yenien yükler. **LAG !** |
| `idlecheck` || Kullanıcı timeout kontrolü yapar . |
| `senduserdata` || Aktif kullanıcı sayısını verir. |
| `addnotification` | `(title) (expiry) (body)` | Bildirim Paneline bir bildirim ekler . `+123` eklentisi ile 123 saniye dayanır |
| `bp` | `(packet)` | Broadcasts a raw JSON packet to all connections. |
| `up` | `(username) (packet)` | Broadcasts a raw JSON packet to all active connections from the user. |
| `f` | `(faction ID) [delete/tag [new tag]/name [new name]]` | Prints information about the faction or changes the tag or name. |

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
