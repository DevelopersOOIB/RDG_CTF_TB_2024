FROM php:7.4-apache

RUN apt-get update && apt-get install sudo ncat -yqq
RUN alias nc="netcat"
RUN echo "www-data ALL=(ALL) NOPASSWD: /usr/bin/awk" >> /etc/sudoers
COPY start-apache /usr/local/bin
RUN chmod 755 /usr/local/bin/start-apache
RUN a2enmod rewrite

# Copy application source
COPY ./site/* /var/www/html/
RUN chown -R www-data:www-data /var/www
RUN chown root:root /var/www/html/root_flag.txt

CMD ["start-apache"]
