#!/bin/bash

if [ $# -eq 0 ]
    then
        python3 exploit.py --url http://localhost:8080/flag-getter-0.0.1-SNAPSHOT/ --cmd 'whoami'
        
    else
        python3 exploit.py --url http://localhost:8080/flag-getter-0.0.1-SNAPSHOT/ --cmd "$1"
fi

exit 0
