CREATE OR REPLACE FUNCTION create_recurring_order()
RETURNS TRIGGER AS $$
DECLARE
    new_delivery_date TIMESTAMP;
    new_order_id UUID;
    old_order_item RECORD;
BEGIN
    IF NEW.order_status = 'DELIVERED' AND OLD.order_status != 'DELIVERED' THEN
        IF NEW.order_type != 'ONCE' THEN
            CASE NEW.order_type
                WHEN 'DAILY' THEN
                    new_delivery_date := NOW() + INTERVAL '1 day';
                WHEN 'WEEKLY' THEN
                    new_delivery_date := NOW() + INTERVAL '7 days';
                WHEN 'MONTHLY' THEN
                    new_delivery_date := NOW() + INTERVAL '1 month';
                ELSE
                    new_delivery_date := NULL;
            END CASE;

            INSERT INTO orders (
                id,
                user_id,
                address_id,
                provider_user_id,
                order_status,
                order_type,
                total_price,
                delivery_date,
                deleted_at
            ) VALUES (
                gen_random_uuid(),
                NEW.user_id,
                NEW.address_id,
                NEW.provider_user_id,
                'APPROVED',
                NEW.order_type,
                NEW.total_price,
                new_delivery_date,
                NULL
            )
            RETURNING id INTO new_order_id;

            FOR old_order_item IN
                SELECT id, product_id, quantity, price_at_purchase, deleted_at
                FROM order_item
                WHERE order_id = OLD.id AND deleted_at IS NULL
            LOOP
                INSERT INTO order_item (
                    id,
                    order_id,
                    product_id,
                    quantity,
                    price_at_purchase,
                    deleted_at
                ) VALUES (
                    gen_random_uuid(),
                    new_order_id,
                    old_order_item.product_id,
                    old_order_item.quantity,
                    old_order_item.price_at_purchase,
                    NULL
                );
            END LOOP;
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trigger_create_recurring_order ON orders;

CREATE TRIGGER trigger_create_recurring_order
AFTER UPDATE ON orders
FOR EACH ROW
EXECUTE FUNCTION create_recurring_order();
