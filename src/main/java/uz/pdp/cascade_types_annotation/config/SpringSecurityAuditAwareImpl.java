
package uz.pdp.cascade_types_annotation.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.pdp.cascade_types_annotation.entity.EmpCustomer;
import java.util.Optional;
import java.util.UUID;


public class SpringSecurityAuditAwareImpl implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&  authentication.isAuthenticated()
                                   && !authentication.getPrincipal().equals("anonymousUser"))
        {
            EmpCustomer user = (EmpCustomer) authentication.getPrincipal();
            return Optional.of(user.getId());
        }
        return Optional.empty();
    }
}

