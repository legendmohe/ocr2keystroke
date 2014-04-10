#!/usr/bin/env python
# encoding: utf-8


import sys
from urlparse import urlparse, parse_qs
from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler 
from autopy import key


def key_stroke(res):
    key.type_string(res)

class TestHTTPHandle(BaseHTTPRequestHandler):
    def do_GET(self):
        buf = 'It works'
        self.protocal_version = "HTTP/1.1"   
        self.send_response(200)     
        self.end_headers()

        res = parse_qs(urlparse(self.path).query)["res"]
        res = " ".join(res)
        print res
        if not len(res) == 0:
            self.wfile.write(res)
            key_stroke(res)
        else:
            self.wfile.write("request error.")
 
if sys.argv[1:]:
    port = int(sys.argv[1])
else:
    port = 8000
server_address = ('192.168.1.100', port)
 
httpd = HTTPServer(server_address, TestHTTPHandle)
 
sa = httpd.socket.getsockname()
print "Serving HTTP on", sa[0], "port", sa[1], "..."
httpd.serve_forever()
