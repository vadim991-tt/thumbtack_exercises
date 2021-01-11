package com.lineate.traineeship;

public class DatabaseEntityService extends BaseEntityService {
    private final EntityRepository entityRepository;

    public DatabaseEntityService(EntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    protected Entity getByName(String name) {
        return entityRepository.getByName(name);
    }

    @Override
    protected boolean save(Entity entity) {
        return entityRepository.save(entity);
    }

    @Override
    protected boolean delete(Entity entity) {
        return entityRepository.delete(entity);
    }
}
