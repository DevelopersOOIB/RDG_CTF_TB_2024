### Задание

Служба безопасности передала на исследование файлы неизвестного назначения. Разберитесь с этим.

Ссылка на вложение: https://disk.yandex.ru/d/-zjvOkcV2Npwpg

### Решение

На входе имеем дамп памяти dump.mem и файл storage.file, который предположительно является криптоконтейнером. 
На последнее указывает ряд признаков, в том числе высокая степень энтропии содержимого указанного файла.

Для анализа предоставленного дампа памяти воспользуемся программным обеспечением
[Volatility](https://github.com/volatilityfoundation/volatility). Извлекаем информацию об операционной системе 
с помощью плагина imageinfo
```powershell 
PS > .\volatility_2.6_win64_standalone.exe -f dump.mem imageinfo

Volatility Foundation Volatility Framework 2.6
INFO    : volatility.debug    : Determining profile based on KDBG search...
          Suggested Profile(s) : Win10x64
                     AS Layer1 : Win10AMD64PagedMemory (Kernel AS)
                     AS Layer2 : FileAddressSpace (dump.mem)
                      PAE type : No PAE
                           DTB : 0x1ab000L
             KUSER_SHARED_DATA : 0xfffff78000000000L
           Image date and time : 2024-03-30 09:10:08 UTC+0000
     Image local date and time : 2024-03-30 20:10:08 +1100
```

Дамп памяти зафиксирован с рабочей станции под управлением операционной системы Windows 10x64. 
Выведем список запущенных процессов с помощью плагина pslist
```powershell
PS > .\volatility_2.6_win64_standalone.exe -f dump.mem pslist --profile Win10x64

Offset(V)          Name                    PID   PPID   Thds     Hnds   Sess  Wow64 Start                          Exit

------------------ -------------------- ------ ------ ------ -------- ------ ------ ------------------------------ ----------------
...
0xffffe00104b3e840 svchost.exe            2528    548      3        0      1      0 2024-03-30 08:52:23 UTC+0000
0xffffe00104bed840 ApplicationFra          932    636      3        0      1      0 2024-03-30 08:52:25 UTC+0000
0xffffe00104eb1840 OneDrive.exe           5020   2396      0 --------      1      0 2024-03-30 08:52:26 UTC+0000   2024-03-30 08:53:24 UTC+0000
0xffffe0010650c840 TrueCrypt.exe          5484   3112      6        0      1      1 2024-03-30 08:52:46 UTC+0000
0xffffe00106b7c840 WmiPrvSE.exe           1692    636      6        0      0      0 2024-03-30 08:58:51 UTC+0000
...
```

В списке процессов обращаем внимание на программное обеспечения для шифрования TrueCrypt. Стоит отметить, что поддержка 
TrueCrypt прекращена в 2014 году. Выведем сводную информацию о примонтированных разделах с помощью плагина 
truecryptsummary
``` powershell
PS > .\volatility_2.6_win64_standalone.exe -f dump.mem --profile=Win10x64 truecryptsummary

Volatility Foundation Volatility Framework 2.6
Process              TrueCrypt.exe at 0xffffe0010650c840 pid 5484
Service              truecrypt state SERVICE_RUNNING
Kernel Module        truecrypt.sys at 0xfffff8018d900000 - 0xfffff8018d941000
Symbolic Link        L: -> \Device\TrueCryptVolumeL mounted 2024-03-30 09:07:22 UTC+0000
Symbolic Link        Volume{e9b762e1-ee71-11ee-9bc5-e4b3187d2098} -> \Device\TrueCryptVolumeL mounted 2024-03-30 09:07:22 UTC+0000
Symbolic Link        L: -> \Device\TrueCryptVolumeL mounted 2024-03-30 09:07:22 UTC+0000
```
Видим статус сервиса, точку монтирования нашего контейнера и т.д. Извлекаем master key с помощью плагина truecryptmaster
```powershell
PS > .\volatility_2.6_win64_standalone.exe -f dump.mem --profile=Win10x64 truecryptmaster -D ./keys

Container: \??\C:\Users\user\Desktop\storage.file
Hidden Volume: No
Removable: No
Read Only: No
Disk Length: 104595456 (bytes)
Host Length: 104857600 (bytes)
Encryption Algorithm: AES
Mode: XTS
Master Key
0xffffe0010808e1a8  9c 21 81 ac 4c 38 7d 46 62 1d 20 87 57 ff a9 5b   .!..L8}Fb...W..[
0xffffe0010808e1b8  0f ee 63 4e d3 a5 9b 3a bb dd ca 97 57 1d ec 1a   ..cN...:....W...
0xffffe0010808e1c8  3c 8d 11 c1 e1 38 10 74 82 c0 16 fd da 37 0b 96   <....8.t.....7..
0xffffe0010808e1d8  05 c3 b8 a5 94 31 6a 02 97 39 5e 1c 57 73 e5 05   .....1j..9^.Ws..
Dumped 64 bytes to ./keys\0xffffe0010808e1a8_master.key
```

Для расшифровки криптоконтейнера воспользуемся проектом [MKDecrypt](https://github.com/AmNe5iA/MKDecrypt.git). 
Указываем путь до криптоконтейнера, точку монтирования и восстановленный ранее master key
```
sudo python MKDecrypt.py storage.file -m /mnt -X 0xffffe0010808e1a8_master.key
```

Переходим в директорию с примонтированным контейнером и обнаруживаем там изображение с флагом
![](files/photo_2024-03-30_19-03-19.jpg)

**Флаг:** RDGCTF{donteverusetruecrypt}

Подробный обзор:
https://codeby.net/threads/rasshifrovyvaem-kriptokontejner-truecrypt-s-pomoschju-volatility-i-mkdecrypt.74360/page-2