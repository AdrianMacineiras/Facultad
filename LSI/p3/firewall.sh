#!/bin/sh

# --------------------------- IPTABLES IPv4 ----------------------------

#Elimino las reglas anteriores
iptables -F
iptables -X
iptables -t nat -F
iptables -t nat -X
iptables -t mangle -F
iptables -t mangle -X

#Politica por defecto (dropear todo)
iptables -P INPUT DROP
iptables -P OUTPUT DROP
iptables -P FORWARD DROP

#Variables

YO=10.10.102.102
COMPANERO=10.10.102.109

DNS=10.10.102.27
WIFI=10.20.0.0/18
VPN=10.30.8.0/21
RED102=10.10.102.0/24

#Acepto todo en la interfaz de loopback
iptables -A INPUT -i lo -j ACCEPT
iptables -A OUTPUT -o lo -j ACCEPT

#Me permito salir por ssh a otra maquina
iptables -A INPUT -p tcp --sport 22 -m state --state ESTABLISHED,RELATED -j ACCEPT
iptables -A OUTPUT -p tcp --dport 22 -m state --state NEW,ESTABLISHED -j ACCEPT

#Permito que se conecte COMPANERO a mi por ssh
iptables -A INPUT -s $COMPANERO -d $YO -p tcp --dport 22 -m hashlimit --hashlimit-upto 2/min --hashlimit-mode srcip --hashlimit-name ssh_ipcompanero_new -m state --state NEW -j ACCEPT
iptables -A INPUT -s $COMPANERO -d $YO -p tcp --dport 22 -m state --state ESTABLISHED -j ACCEPT
iptables -A OUTPUT -s $YO -d $COMPANERO -p tcp --sport 22 -m state --state RELATED,ESTABLISHED -j ACCEPT

#Permitir ssh a traves de VPN
iptables -A INPUT -s $VPN -d $YO -p tcp --dport 22 -m hashlimit --hashlimit-upto 2/min --hashlimit-mode srcip --hashlimit-name ssh_vpn_new -m state --state NEW -j ACCEPT
iptables -A INPUT -s $VPN -d $YO -p tcp --dport 22 -m state --state ESTABLISHED -j ACCEPT
iptables -A OUTPUT -s $YO -d $VPN -p tcp --sport 22 -m state --state RELATED,ESTABLISHED -j ACCEPT

#Permitir ssh a través de WIFI
iptables -A INPUT -s $WIFI -d $YO -p tcp --dport 22 -m hashlimit --hashlimit-upto 2/min --hashlimit-mode srcip --hashlimit-name ssh_wifi_new -m state --state NEW -j ACCEPT
iptables -A INPUT -s $WIFI -d $YO -p tcp --dport 22 -m state --state ESTABLISHED -j ACCEPT
iptables -A OUTPUT -s $YO -d $WIFI -p tcp --sport 22 -m state --state RELATED,ESTABLISHED -j ACCEPT

#Permitir salir y entrar para las peticiones DNS
iptables -A INPUT -s $DNS -d $YO -p udp --sport 53 -m hashlimit --hashlimit-upto 2/sec --hashlimit-mode srcport --hashlimit-name dns -j ACCEPT
iptables -A OUTPUT -s $YO -d $DNS -p udp --dport 53 -j ACCEPT

#Limitar ICMPs
iptables -A INPUT -p icmp -m hashlimit --hashlimit-upto 5/min --hashlimit-mode srcip --hashlimit-name udp_icmp -j ACCEPT
iptables -A OUTPUT -p icmp -j ACCEPT

#Permitir a COMPANERO entrar por NTP (puerto 123,yo soy CLIENTE)
iptables -A INPUT -i eth0 -s $COMPANERO -d $YO -p udp --sport 123 -m hashlimit --hashlimit-upto 1/sec --hashlimit-mode srcip --hashlimit-name ntp -j ACCEPT
iptables -A OUTPUT -i eth0 -s $YO -d $COMPANERO -p udp --dport 123 -j ACCEPT


#Permitir rsyslog (puerto 514,yo soy servidor)
iptables -A INPUT -s $COMPANERO -d $YO -p tcp --dport 514 -m state --state RELATED,ESTABLISHED -j ACCEPT
iptables -A OUTPUT -s $YO -d $COMPANERO -p tcp --sport 514 -m state --state NEW,ESTABLISHED -j ACCEPT

#Permito conexiones mías al servidor de COMPANERO
iptables -A INPUT -s $COMPANERO -d $YO -p tcp --sport 80 -m state --state RELATED,ESTABLISHED -j ACCEPT
iptables -A OUTPUT -s $YO -d $COMPANERO -p tcp --dport 80 -m state --state NEW,ESTABLISHED -j ACCEPT
	
iptables -A INPUT -s $COMPANERO -d $YO -p tcp --sport 443 -m state --state RELATED,ESTABLISHED -j ACCEPT
iptables -A OUTPUT -s $YO -d $COMPANERO -p tcp --dport 443 -m state --state NEW,ESTABLISHED -j ACCEPT

#Permito conexiones de COMPANERO a mi servidor
iptables -A INPUT -s $COMPANERO -d $YO -p tcp --dport 80  -m hashlimit --hashlimit-upto 10/min --hashlimit-mode srcip --hashlimit-name http_companero_new -m state --state NEW -j ACCEPT
iptables -A INPUT -s $COMPANERO -d $YO -p tcp --dport 80 -m hashlimit --hashlimit-upto 150/sec --hashlimit-mode srcip --hashlimit-name http_companero_established -m state --state ESTABLISHED -j ACCEPT
iptables -A OUTPUT -s $YO -d $COMPANERO -p tcp --sport 80 -m state --state RELATED,ESTABLISHED -j ACCEPT

iptables -A INPUT -s $COMPANERO -d $YO -p tcp --dport 443  -m hashlimit --hashlimit-upto 10/min --hashlimit-mode srcip --hashlimit-name https_companero_new -m state --state NEW -j ACCEPT
iptables -A INPUT -s $COMPANERO -d $YO -p tcp --dport 443 -m hashlimit --hashlimit-upto 150/sec --hashlimit-mode srcip --hashlimit-name http_companero_established -m state --state ESTABLISHED -j ACCEPT
iptables -A OUTPUT -s $YO -d $COMPANERO -p tcp --sport 443 -m state --state RELATED,ESTABLISHED -j ACCEPT

#Permito conexiones a mi servidor http/https a traves de la red 102
iptables -A INPUT -s $RED102 -d $YO -p tcp --dport 80 -m hashlimit --hashlimit-upto 4/min --hashlimit-mode dstport --hashlimit-name http_102_new -m state --state NEW -j ACCEPT
iptables -A INPUT -s $RED102 -d $YO -p tcp --dport 80 -m hashlimit --hashlimit-upto 100/sec --hashlimit-mode dstport --hashlimit-name http_102_established -m state --state ESTABLISHED -j ACCEPT
iptables -A OUTPUT -s $YO -d $RED102 -p tcp --sport 80 -m state --state RELATED,ESTABLISHED -j ACCEPT

iptables -A INPUT -s $RED102 -d $YO -p tcp --dport 443 -m hashlimit --hashlimit-upto 4/min --hashlimit-mode dstport --hashlimit-name https_102_new -m state --state NEW -j ACCEPT
iptables -A INPUT -s $RED102 -d $YO -p tcp --dport 443 -m hashlimit --hashlimit-upto 100/sec --hashlimit-mode dstport --hashlimit-name https_102_established -m state --state ESTABLISHED -j ACCEPT
iptables -A OUTPUT -s $YO -d $RED102 -p tcp --sport 443 -m state --state RELATED,ESTABLISHED -j ACCEPT

#Permito conexiones a mi servidor a traves de las redes WIFI
iptables -A INPUT -s $WIFI -d $YO -p tcp --dport 80 -m hashlimit --hashlimit-upto 5/min --hashlimit-mode dstport --hashlimit-name http_wifi_new -m state --state NEW -j ACCEPT
iptables -A INPUT -s $WIFI -d $YO -p tcp --dport 80 -m hashlimit --hashlimit-upto 100/sec --hashlimit-mode dstport --hashlimit-name http_wifi_established -m state --state ESTABLISHED -j ACCEPT
iptables -A OUTPUT -s $YO -d $WIFI -p tcp --sport 80 -m state --state RELATED,ESTABLISHED -j ACCEPT

iptables -A INPUT -s $WIFI -d $YO -p tcp --dport 443 -m hashlimit --hashlimit-upto 5/min --hashlimit-mode dstport --hashlimit-name https_wifi_new -m state --state NEW -j ACCEPT
iptables -A INPUT -s $WIFI -d $YO -p tcp --dport 443 -m hashlimit --hashlimit-upto 100/sec --hashlimit-mode dstport --hashlimit-name https_wifi_established -m state --state ESTABLISHED -j ACCEPT
iptables -A OUTPUT -s $YO -d $WIFI -p tcp --sport 443 -m state --state RELATED,ESTABLISHED -j ACCEPT

#Permito conexiones a mi servidor a traves de VPN
iptables -A INPUT -s $VPN  -d $YO -p tcp --dport 80 -m hashlimit --hashlimit-upto 5/min --hashlimit-mode dstport --hashlimit-name http_vpn_new -m state --state NEW -j ACCEPT
iptables -A INPUT -s $VPN  -d $YO -p tcp --dport 80 -m hashlimit --hashlimit-upto 100/sec --hashlimit-mode dstport --hashlimit-name http_vpn_established -m state --state ESTABLISHED -j ACCEPT
iptables -A OUTPUT -s $YO -d $VPN -p tcp --sport 80 -m state --state RELATED,ESTABLISHED -j ACCEPT

iptables -A INPUT -s $VPN  -d $YO -p tcp --dport 443 -m hashlimit --hashlimit-upto 5/min --hashlimit-mode dstport --hashlimit-name https_vpn_new -m state --state NEW -j ACCEPT
iptables -A INPUT -s $VPN  -d $YO -p tcp --dport 443 -m hashlimit --hashlimit-upto 100/sec --hashlimit-mode dstport --hashlimit-name https_vpn_established -m state --state ESTABLISHED -j ACCEPT
iptables -A OUTPUT -s $YO -d $VPN -p tcp --sport 443 -m state --state RELATED,ESTABLISHED -j ACCEPT

#Permito conexiones a mi servidor a traves de ETHERNET
iptables -A INPUT -s $ETHERNET  -d $YO -p tcp --dport 80 -m hashlimit --hashlimit-upto 5/min --hashlimit-mode dstport --hashlimit-name http_ethernet_new -m state --state NEW -j ACCEPT
iptables -A INPUT -s $ETHERNET  -d $YO -p tcp --dport 80 -m hashlimit --hashlimit-upto 100/sec --hashlimit-mode dstport --hashlimit-name http_ethernet_established -m state --state ESTABLISHED -j ACCEPT
iptables -A OUTPUT -s $YO -d $ETHERNET -p tcp --sport 80 -m state --state RELATED,ESTABLISHED -j ACCEPT

iptables -A INPUT -s $ETHERNET  -d $YO -p tcp --dport 443 -m hashlimit --hashlimit-upto 5/min --hashlimit-mode dstport --hashlimit-name https_ethernet_new -m state --state NEW -j ACCEPT
iptables -A INPUT -s $ETHERNET  -d $YO -p tcp --dport 443 -m hashlimit --hashlimit-upto 100/sec --hashlimit-mode dstport --hashlimit-name https_ethernet_established -m state --state ESTABLISHED -j ACCEPT
iptables -A OUTPUT -s $YO -d $ETHERNET -p tcp --sport 443 -m state --state RELATED,ESTABLISHED -j ACCEPT

#----------------------------------------------PARA IPv6(aunque está deshabilitado, dropearé todo igualmente)-------------------------------------------

#Borro reglas antiguas de IPv6
ip6tables -F
ip6tables -X
ip6tables -t mangle -F
ip6tables -t mangle -X

ip6tables -P INPUT DROP
ip6tables -P OUTPUT DROP
ip6tables -P FORWARD DROP
