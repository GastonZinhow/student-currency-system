FROM node:18-alpine AS build-frontend
WORKDIR /app

COPY package*.json ./
RUN npm install --frozen-lockfile

COPY . ./

RUN npm run build

FROM node:18-alpine
WORKDIR /app

COPY --from=build-frontend /app/node_modules ./node_modules
COPY --from=build-frontend /app/.next ./.next
COPY --from=build-frontend /app/public ./public
COPY --from=build-frontend /app/package*.json ./

EXPOSE 3000

CMD ["npm", "start"]