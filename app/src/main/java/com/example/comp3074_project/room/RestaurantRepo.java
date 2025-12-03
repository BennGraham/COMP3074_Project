package com.example.comp3074_project.room;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RestaurantRepo {
    private RestaurantDao restaurantDao;
    private TagDao tagDao;
    private RatingDao ratingDao;

    private LiveData<List<Restaurant>> allRestaurants;
    private List<Tag> allTags;
    private List<RestaurantWithTags> allRestaurantsWithTags;

    public RestaurantRepo(Application application) {
        ProjectDatabase db = ProjectDatabase.getInstance(application);
        restaurantDao = db.restaurantDao();
        tagDao = db.tagDao();
        ratingDao = db.ratingDao();

        allRestaurants = restaurantDao.getAllRestaurants();
        allTags = tagDao.getAllTags();
        allRestaurantsWithTags = restaurantDao.getAllRestaurantsWithTags();
    }

    public LiveData<List<Restaurant>> getAllRestaurants() {
        return allRestaurants;
    }

    public Restaurant getRestaurantById(long restaurantId) {
        return restaurantDao.getRestaurantById(restaurantId);
    }

    public RestaurantWithTags getRestaurantWithTagsById(long restaurantId) {
        return restaurantDao.getRestaurantWithTags(restaurantId);
    }

    public List<RestaurantWithTags> getAllRestaurantsWithTags() {
        return allRestaurantsWithTags;
    }

    public void insert(Restaurant restaurant, String tagsInput) {
        ProjectDatabase.databaseExecutor.execute(() -> {
            long restaurantId = restaurantDao.insert(restaurant);
            if (restaurantId != -1) {
                if (tagsInput != null && !tagsInput.isEmpty()) {
                    String[] tags = tagsInput.split(",");
                    for (String tag : tags) {
                        String tagName = tag.trim();
                        if (!tagName.isEmpty()) {
                            addTagToRestaurant(restaurantId, tagName);
                        }
                    }
                }
            }
        });
    }

    public boolean update(Restaurant restaurant) {
        try {
            ProjectDatabase.databaseExecutor.execute(() -> restaurantDao.update(restaurant));
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public boolean delete(Restaurant restaurant) {
        try {
            ProjectDatabase.databaseExecutor.execute(() -> restaurantDao.delete(restaurant));
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public boolean deleteAll() {
        try {
            ProjectDatabase.databaseExecutor.execute(() -> restaurantDao.deleteAllRestaurants());
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public LiveData<List<Restaurant>> searchRestaurantsByName(String searchQuery) {
        return restaurantDao.searchRestaurantsByName(searchQuery);
    }

    public List<Tag> getAllTags() {
        return allTags;
    }

    public TagWithRestaurants getTagWithRestaurants(long tagId) {
        return tagDao.getTagWithRestaurants(tagId);
    }

    public List<TagWithRestaurants> getAllTagsWithRestaurants() {
        return tagDao.getAllTagsWithRestaurants();
    }

    public TagWithRestaurants getTagWithRestaurantsByTagName(String tagName) {
        return tagDao.getTagWithRestaurantsByTagName(tagName);
    }

    public boolean insert(Tag tag) {
        ProjectDatabase.databaseExecutor.execute(() -> tagDao.insert(tag));
        return tag.getId() != -1;
    }

    public boolean addTagToRestaurant(long restaurantId, String tagName) {
        ProjectDatabase.databaseExecutor.execute(() -> {
            long tagId = tagDao.getTagIdByName(tagName);
            if (tagId == 0) {
                Tag newTag = new Tag(tagName);
                tagDao.insert(newTag);
                tagId = tagDao.getTagIdByName(tagName);
            }
            restaurantDao.insertRestaurantTag(new RestaurantTag(restaurantId, tagId));
        });
        return true;
    }

    public boolean removeTagFromRestaurant(long restaurantId, long tagId) {
        try {
            ProjectDatabase.databaseExecutor.execute(() -> restaurantDao.deleteRestaurantTag(
                    restaurantId,
                    tagId
            ));
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public boolean insertRating(Rating rating) {
        try {
            ProjectDatabase.databaseExecutor.execute(() -> ratingDao.insert(rating));
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public List<Rating> getRatingsByRestaurantId(long restaurantId) {
        return ratingDao.getRatingsByRestaurantId(restaurantId);
    }

    public Float getAverageRatingByRestaurantId(long restaurantId) {
        return ratingDao.getAverageRatingByRestaurantId(restaurantId);
    }

    public boolean updateRestaurantRating(long restaurantId) {
        try {
            ProjectDatabase.databaseExecutor.execute(() -> {
                Float avgRating = ratingDao.getAverageRatingByRestaurantId(restaurantId);
                if (avgRating != null) {
                    Restaurant restaurant = restaurantDao.getRestaurantById(restaurantId);
                    if (restaurant != null) {
                        restaurant.setRating(avgRating);
                        restaurantDao.update(restaurant);
                    }
                }
            });
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public LiveData<List<Restaurant>> searchRestaurantsByNameOrTag(String query) {
        return restaurantDao.searchRestaurantsByNameOrTag(query);
    }
}
