FROM node:18-alpine AS base

FROM base AS deps
WORKDIR /app
COPY package.json package-lock.json* ./
RUN npm ci --only=production

FROM base AS dev
WORKDIR /app

RUN addgroup --system --gid 1001 nodejs && adduser --system --uid 1001 nextjs

COPY package.json package-lock.json* ./
RUN npm ci

COPY . .
RUN mkdir -p .next && chown -R nextjs:nodejs /app

USER nextjs

EXPOSE 3000

CMD ["npm", "run", "dev"]