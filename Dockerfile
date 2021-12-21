FROM node:12.22.1-buster-slim as build

WORKDIR /opt/app-root/yarn

COPY package.json ./

RUN yarn

COPY . ./

RUN yarn build

 
FROM registry.redhat.io/rhel8/nginx-116:1-95

ADD ./nginx.conf "${NGINX_CONF_PATH}"

COPY --chown=default:root --from=build /opt/app-root/yarn/build/ .

USER 0 
RUN chown -R 1001:0 /opt/app-root/src
USER 1001

EXPOSE 8080

CMD nginx -g "daemon off;"
