library libcurl_x64;

{$mode objfpc}{$H+}

uses
  windows, sysutils
  { you can add units after this };

var
  handle_id:cardinal;

procedure LogInit();
var
  f:textfile;
begin
  assignfile(f, 'Log.txt');
  rewrite(f);
  closefile(f);
end;

procedure Log(s:string);
var
  f:textfile;
begin
  assignfile(f, 'Log.txt');
  append(f);
  writeln(f,s);
  closefile(f);
end;

procedure curl_slist_free_all(); cdecl;
begin
  Log('start curl_slist_free_all');
  Log('end curl_slist_free_all');
end;

function curl_easy_init():cardinal; cdecl;
begin
  Log('start curl_easy_init');
  result:=handle_id;
  handle_id:=handle_id+1;
  Log('end curl_easy_init');
end;

procedure curl_easy_cleanup(); cdecl;
begin
  Log('start curl_easy_cleanup');
  Log('end curl_easy_cleanup');
end;

type TWrCb = function (ptr:PAnsiChar; size:uintptr; nitems:uintptr; userdata:uintptr):uintptr; cdecl;

var
  wrcb:TWrCb;
  wrcb_data:uintptr;
  nonce_str:string;
function curl_easy_setopt(hndl:cardinal; opt:integer; value:uintptr):cardinal; cdecl;
var
  nonce_pos:integer;
  url:string;
begin
  Log('start curl_easy_setopt, hndl='+inttostr(hndl)+', opt='+inttostr(opt));
  if opt = 10002 then begin
     url:=PAnsiChar(value);
     Log('CURLOPT_URL: '+url);
     nonce_pos:=Pos('nonce',url);
     if nonce_pos > 0 then begin
        nonce_str:=rightstr(PAnsiChar(value), length(url)-nonce_pos-5);
        Log('Nonce is '+nonce_str);
     end else begin
       nonce_str:='';
     end;
  end else if opt = 20011 then begin
     Log('CURLOPT_WRITEFUNCTION: '+inttohex(value));
     wrcb:=TWrCb(value);
  end else if opt = 10001 then begin
     Log('CURLOPT_WRITEDATA: '+inttohex(value));
     wrcb_data:=value;
  end;
  Log('end curl_easy_setopt');
  result:=0;
end;

function curl_easy_perform(hndl:cardinal):cardinal; cdecl;
var
  s:string;
  p:PAnsiChar;
  l:integer;
begin
  s:='{is_success: 1,timestamp:"';
  s:=s+inttostr(2024+hndl);
  s:=s+'-03-21T18:25:43-05:00",session_id:"1", user_id:"RDGCTF", cmd_id:0,data:"qwer"';

  if length(nonce_str)> 0 then begin
    s:=s+',nonce:'+nonce_str;
  end;
  s:=s+'}';

  p:=PAnsiChar(s);
  l:=length(s);
  Log('start curl_easy_perform, hndl='+inttostr(hndl));
  wrcb(p, l, 1, wrcb_data);
  Log('end curl_easy_perform');
  result:=0;
end;

function curl_easy_getinfo(hndl:cardinal; info:cardinal; res:pcardinal):cardinal; cdecl;
begin
  Log('start curl_easy_getinfo, hndl='+inttostr(hndl)+', info='+inttostr(info));
  if info = 2097154 then begin
     Log('CURLINFO_RESPONSE_CODE');
     res^:=200;
  end;
  Log('end curl_easy_getinfo');
  result:=0;
end;

exports curl_easy_getinfo, curl_easy_perform, curl_easy_setopt, curl_easy_cleanup, curl_easy_init, curl_slist_free_all;

begin
  handle_id:=1;
  LogInit();
end.

