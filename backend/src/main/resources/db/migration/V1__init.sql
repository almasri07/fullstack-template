-- V1__init.sql
-- Initial database schema: global users/auth, tenant-scoped tasks/audit logs

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Tenants table

CREATE TABLE tenants (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name       VARCHAR(255) NOT NULL,
    slug       VARCHAR(255) NOT NULL UNIQUE,
    subscription_tier VARCHAR(50) NOT NULL DEFAULT 'FREE',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
    );

-- Users table
CREATE TABLE users (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email      VARCHAR(255) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name  VARCHAR(100),
    role       VARCHAR(50)  NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Tasks table
CREATE TABLE tasks (
   id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   tenant_id  UUID NOT NULL REFERENCES tenants(id),
   title      VARCHAR(255) NOT NULL,
   description TEXT,
   priority   VARCHAR(50) NOT NULL DEFAULT 'MEDIUM',
   status     VARCHAR(50) NOT NULL DEFAULT 'TODO',
   deadline   TIMESTAMP,
   assignee_id   UUID REFERENCES users(id),
   created_by   UUID NOT NULL REFERENCES users(id),
   created_at TIMESTAMP NOT NULL DEFAULT NOW(),
   updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Audit logs table
CREATE TABLE audit_logs (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id  UUID NOT NULL REFERENCES tenants(id),
    user_id    UUID NOT NULL REFERENCES users(id),
    action     VARCHAR(255) NOT NULL,
    payload    JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
    );


-- Indexes
CREATE INDEX idx_tasks_tenant_id ON tasks(tenant_id);
CREATE INDEX idx_tasks_assignee_id ON tasks(assignee_id);
CREATE INDEX idx_audit_logs_tenant_id ON audit_logs(tenant_id);

-- Partial index: only index active (non-completed) tasks per tenant

CREATE INDEX idx_tasks_active_per_tenant
    ON tasks(tenant_id, status)
    WHERE status != 'DONE';
