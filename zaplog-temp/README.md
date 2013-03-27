#ZapLog
======
Command Line Log Tool written in Java.

##Usage
=====
Parameter | Description
--- | ---
`-a, -audit` | includes report from plugins
`-c, -chronological` | sort log lines in chronological order
`-f, -format` | formats the log lines
`-ln, -linenumbers` | includes line numbers
`-nf, -nofilenames` | doesn`t include filenames in output
`-nh, -noheader` | no header in output
`-nologoutput` | output only includes header
`-ns, -nostrings` | filter out specific strings
`-o, -output` | output to file
`-p, -pretty` | (Linux) enables colors for different log files 
`-r` | recursively finds logs in folders
`regex` | filter log lines by regex
`s` | filter log lines by strings
`tail` | (Experiemental) tails the output


##Examples
========
####Basic
 ```
 ./zaplog var/mylog1.log logs/mylog2.log 
 ```
  
####Chronological Ordering
```
./zaplog var/* -c
```
	
####File Output
```
 ./zaplog var/* -c -f -o=result.txt
```
	
####Pretty Print (Linux)
```
./zaplog var/mylog1.log var/mylog2.log -c -f -p | less -R
```  
	
####Regex/String Filtering
```
./zaplog var/* -s=user1 -ns=WATCHLIST -regex=.*
```

####Only Show Log Text
```
./zaplog var/* -nf -nh -nt
```
  
####Grep with Colors (Linux)
```
./zaplog var/* -f -c | grep --color=always user | less -R
```	

####Print Output from Plugins
```
./zaplog var/* -a
```
