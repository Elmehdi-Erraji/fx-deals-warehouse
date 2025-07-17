-- Create fx_deals table
CREATE TABLE fx_deals (
    id BIGSERIAL PRIMARY KEY,
    deal_unique_id VARCHAR(255) NOT NULL UNIQUE,
    from_currency VARCHAR(3) NOT NULL,
    to_currency VARCHAR(3) NOT NULL,
    deal_timestamp TIMESTAMP NOT NULL,
    deal_amount DECIMAL(19,4) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_fx_deals_deal_unique_id ON fx_deals(deal_unique_id);
CREATE INDEX idx_fx_deals_from_currency ON fx_deals(from_currency);
CREATE INDEX idx_fx_deals_to_currency ON fx_deals(to_currency);
CREATE INDEX idx_fx_deals_deal_timestamp ON fx_deals(deal_timestamp);
CREATE INDEX idx_fx_deals_created_at ON fx_deals(created_at);

-- Add constraints
ALTER TABLE fx_deals ADD CONSTRAINT chk_deal_amount_positive CHECK (deal_amount > 0);
ALTER TABLE fx_deals ADD CONSTRAINT chk_from_currency_length CHECK (LENGTH(from_currency) = 3);
ALTER TABLE fx_deals ADD CONSTRAINT chk_to_currency_length CHECK (LENGTH(to_currency) = 3);
ALTER TABLE fx_deals ADD CONSTRAINT chk_different_currencies CHECK (from_currency != to_currency);

