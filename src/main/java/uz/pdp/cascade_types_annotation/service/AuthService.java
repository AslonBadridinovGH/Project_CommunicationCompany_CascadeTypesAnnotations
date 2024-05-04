package uz.pdp.cascade_types_annotation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.cascade_types_annotation.repository.EmpCustomerRepository;


@Service
public class AuthService implements UserDetailsService {

    @Autowired
    EmpCustomerRepository employeeRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return employeeRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username+"not found"));
    }


}
