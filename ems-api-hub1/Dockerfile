FROM swaggerapi/swagger-ui:v5.17.14

RUN apk update && apk add bash && apk add yarn
RUN yarn global add @stoplight/prism-cli@4

COPY swagger /usr/share/nginx/html/swagger
COPY swagger /usr/src/prism/

ENV CONFIG_URL=./swagger/swagger-config.json
ENV VALIDATOR_URL=127.0.0.1
RUN rm /etc/nginx/nginx.conf
COPY nginx.conf /etc/nginx/nginx.conf

COPY --from=swaggerapi/swagger-ui:v5.17.14 /usr/share/nginx/html /usr/share/nginx/html

EXPOSE 8080
ENTRYPOINT  prism mock -p 4010 --host 0.0.0.0 /usr/src/prism/service/api.yaml & \
 /docker-entrypoint.sh nginx -g 'daemon off;'
