### STAGE 1: Build ###
FROM node:18.15.0 AS build
WORKDIR /usr/src/app
COPY package.json ./
RUN npm install
COPY src src
COPY index.html index.html
COPY vite.config.js vite.config.js
COPY public public

#package dell'applicazione
RUN npm run build 


### STAGE 2: Run ###
#HTTP Server nginx

FROM nginx:alpine  
COPY default.conf /etc/nginx/conf.d/default.conf
COPY --from=build /usr/src/app/dist /usr/share/nginx/html
EXPOSE 5173