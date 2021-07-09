package repository;
import entity.FacebookPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacebookPostRepository extends JpaRepository<FacebookPost, Integer> {
}
